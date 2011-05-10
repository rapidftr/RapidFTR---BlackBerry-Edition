package com.rapidftr.services;

import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.MockStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.model.ChildStatus;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.net.RequestFactory;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Hashtable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChildPhotoUpdateListenerTest {
    private RequestCallBack callback;
    private ChildrenRecordStore store;
    private ChildPhotoUpdateListener listener;
    private static final String SAVED_LOCATION = "/foo/bar.jpg";

    @Before
    public void setup() {
        store = new ChildrenRecordStore(new MockStore());
        callback = mock(RequestCallBack.class);
        createListener(false);
    }

    private void createListener(boolean currentSyncStatus) {
        listener = new ChildPhotoUpdateListener(callback, 1, currentSyncStatus, store) {

            @Override
            protected String savePhoto(Child child, byte[] data) throws IOException {
                return SAVED_LOCATION;
            }
        };
    }

    @Test
    public void shouldSavePhotoKeyOnSuccessfulResponse() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);
        Result result = Result.fromContent("{\"current_photo_key\":\"updatedvalue\"}", "application/json");
        Response response = new Response(result, 200);

        listener.done(requestContext, response);

        final Children children = store.getAll();
        final Child childFromStore = children.toArray()[0];
        assertEquals(SAVED_LOCATION, childFromStore.getPhotoKey());
    }

    @Test
    public void shouldInvokeProcessSuccessOnSuccess() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);
        Result result = Result.fromContent("{\"current_photo_key\":\"updatedvalue\"}", "application/json");
        Response response = new Response(result, 200);

        listener.done(requestContext, response);
        verify(callback).onProcessSuccess();
    }

    @Test
    public void shouldSetSyncFailedOnFailure() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        listener.done(requestContext, RequestFactory.failedResponse());

        final Children children = store.getAll();
        final Child childFromStore = children.toArray()[0];
        assertEquals(ChildStatus.SYNC_FAILED, childFromStore.childStatus());
    }

    @Test
    public void shouldInvokeProcessFailOnFailure() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        listener.done(requestContext, RequestFactory.failedResponse());

        verify(callback).onProcessFail(anyString());
    }

    @Test
    public void shouldInvokeProcessFailIfOriginalSyncStatusIsFalse() throws Exception {
        createListener(true);
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);
        Result result = Result.fromContent("{\"current_photo_key\":\"updatedvalue\"}", "application/json");
        Response response = new Response(result, 200);

        listener.done(requestContext, response);
        verify(callback).onProcessFail(anyString());

    }

}
