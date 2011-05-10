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
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import java.util.Hashtable;
import java.util.Vector;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChildSyncListenerTest {
    private ChildPhotoUpdater photoUpdater;
    private ChildrenRecordStore store;
    private ChildSyncListener listener;
    private RequestCallBack requestCallback;

    @Before
    public void setup() {
        photoUpdater = mock(ChildPhotoUpdater.class);
        store = new ChildrenRecordStore(new MockStore());
        requestCallback = mock(RequestCallBack.class);
        listener = new ChildSyncListener(requestCallback,
                1, store, photoUpdater);
    }

    @Test
    public void shouldUpdateChildFieldsOnRequestSuccess() throws Exception {
        Child child = ChildFactory.existingChild("id");
        child.setField("somefield", "original value");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        listener.done(requestContext, successfulResponse());

        final Children children = store.getAll();
        final Child childFromStore = children.toArray()[0];
        assertEquals("updatedvalue", childFromStore.getField("somefield"));
        assertEquals(ChildStatus.SYNCED, childFromStore.childStatus());
    }

    @Test
    public void shouldSetSyncFailedOnFailure() throws Exception {
        Child child = ChildFactory.existingChild("id");

        listener.done(RequestFactory.getRequestContext(child), RequestFactory.failedResponse());

        final Children children = store.getAll();
        final Child childFromStore = children.toArray()[0];
        assertEquals(ChildStatus.SYNC_FAILED, childFromStore.childStatus());
    }

    @Test
    public void shouldDecrementTotalsOnSuccess() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        assertEquals(1, listener.getTotal());

        listener.done(requestContext, successfulResponse());

        assertEquals(0, listener.getTotal());
    }

    @Test
    public void shouldDecrementTotalsOnFailure() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        assertEquals(1, listener.getTotal());

        listener.done(requestContext, RequestFactory.failedResponse());

        assertEquals(0, listener.getTotal());
    }

    @Test
    public void shouldCallProcessSuccessOnSuccess() throws Exception {
        Child child = ChildFactory.existingChild("id");
        child.setField("somefield", "original value");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        listener.done(requestContext, successfulResponse());

        verify(requestCallback).onProcessSuccess();
    }

    @Test
    public void shouldCallProcessFailOnFailure() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);

        listener.done(requestContext, RequestFactory.failedResponse());

        verify(requestCallback).onProcessFail(anyString());
    }

    @Test
    public void shouldCallPhotoUpdatesIfNeeded() throws Exception {
        Child child = ChildFactory.existingChild("id");
        Hashtable requestContext = RequestFactory.getRequestContext(child);
        Result result = Result.fromContent("{\"current_photo_key\":\"updatedvalue\"}", "application/json");
        Response response = new Response(result, 200);

        listener.done(requestContext, response);

        Vector childrenWithPhotos = new Vector();
        childrenWithPhotos.addElement(child);
        verify(photoUpdater).doUpdates(childrenWithPhotos, requestCallback, false);
    }

    private Response successfulResponse() throws ResultException {
        Result result = Result.fromContent("{\"somefield\":\"updatedvalue\"}", "application/json");
        Response response = new Response(result, 200);
        return response;
    }

}
