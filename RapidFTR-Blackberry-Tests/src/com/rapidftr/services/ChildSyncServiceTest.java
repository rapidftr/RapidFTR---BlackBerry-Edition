package com.rapidftr.services;

import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.MockStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.model.ChildStatus;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Hashtable;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChildSyncServiceTest {
    private HttpService httpService;
    private ChildrenRecordStore store;
    private ChildSyncService childService;
    private ChildPhotoUpdater photoUpdater;

    @Before
    public void setUp() {
        httpService = mock(HttpService.class);
        photoUpdater = mock(ChildPhotoUpdater.class);
        store = new ChildrenRecordStore(new MockStore());
        childService = new ChildSyncService(httpService, store, photoUpdater);
    }

    @Test
    public void shouldUploadNewChildRecordUsingPost() {

        Child child = ChildFactory.newChild();

        PostData postData = child.getPostData();

        Arg multiPart = new Arg("Content-Type",
                "multipart/form-data;boundary=" + postData.getBoundary());

        Arg json = HttpUtility.HEADER_ACCEPT_JSON;

        Arg[] httpArgs = {multiPart, json};

        Hashtable requestContext = new Hashtable();
        requestContext.put(childService.PROCESS_STATE, "Uploading [" + 1 + "/" + 1 + "]");
        requestContext.put(childService.CHILD_TO_SYNC, child);

        childService.syncChildRecord(child);

        verify(httpService).post(eq("children"), eq((Arg[]) null), eq(httpArgs), (RequestListener) any(),
                eq(postData), eq(requestContext));
    }

    @Test
    public void shouldUploadUpdatedRecordUsingPut() {
        Child child = ChildFactory.existingChild("id");
        child.setField("somefield", "original value");
        child.setField("somefield", "updated value");

        PostData postData = child.getPostData();

        Arg multiPart = new Arg("Content-Type",
                "multipart/form-data;boundary=" + postData.getBoundary());

        Arg json = HttpUtility.HEADER_ACCEPT_JSON;

        Arg[] httpArgs = {multiPart, json};

        Hashtable requestContext = new Hashtable();
        requestContext.put(childService.PROCESS_STATE, "Uploading [" + 1 + "/" + 1 + "]");
        requestContext.put(childService.CHILD_TO_SYNC, child);

        childService.syncChildRecord(child);
        verify(httpService).put(eq("children/id"), eq((Arg[]) null), eq(httpArgs),
                (RequestListener) any(),
                eq(postData), eq(requestContext));
    }

    @Test
    public void shouldUpdateChildFieldsOnRequestSuccess() throws IOException {
        Child child = ChildFactory.existingChild("id");
        child.setField("somefield", "original value");
        Hashtable requestContext = getRequestContext(child);
        Result result = Result.fromContent("{\"somefield\":\"updatedvalue\"}", "application/json");
        Response response = new Response(result, 200);

        childService.onRequestSuccess(requestContext, response);

        final Children children = store.getAll();
        final Child childFromStore = children.toArray()[0];
        assertEquals("updatedvalue", childFromStore.getField("somefield"));
        assertEquals(ChildStatus.SYNCED, childFromStore.childStatus());
    }

    @Test
    public void shouldSetSyncFailedOnFailure() {
        Child child = ChildFactory.existingChild("id");
        childService.onRequestFailure(getRequestContext(child), new Exception());

        final Children children = store.getAll();
        final Child childFromStore = children.toArray()[0];
        assertEquals(ChildStatus.SYNC_FAILED, childFromStore.childStatus());
    }

    private Hashtable getRequestContext(Child child) {
        Hashtable requestContext = new Hashtable();
        requestContext.put(childService.CHILD_TO_SYNC, child);
        requestContext.put(childService.PROCESS_STATE, "some state");
        return requestContext;
    }

}
