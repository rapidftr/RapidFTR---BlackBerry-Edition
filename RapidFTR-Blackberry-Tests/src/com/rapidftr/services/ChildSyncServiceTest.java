package com.rapidftr.services;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.Key;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.MockStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.DateFormatter;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;

public class ChildSyncServiceTest {
    private HttpService httpService;
    private ChildrenRecordStore store;
    private ChildSyncService childService;
    private ChildPhotoUpdater photoUpdater;

    @Before
    public void setUp() {
        httpService = mock(HttpService.class);
        photoUpdater = mock(ChildPhotoUpdater.class);
        DateFormatter dateFormatter = mock(DateFormatter.class);
        store = new ChildrenRecordStore(new MockStore(new Key("childrenrecord")));
        childService = new ChildSyncService(httpService, store, photoUpdater, dateFormatter);
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

        verify(httpService).post(eq("children"), eq((Arg[]) null), eq(httpArgs), (ChildSyncListener) any(),
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
                (ChildSyncListener) any(),
                eq(postData), eq(requestContext));
    }

    @Test
    public void shouldUploadFalggedRecordUsingPut() {
        Child child = ChildFactory.existingChild("id");
        child.setField("somefield", "original value");
        child.syncFailed("Network Failure");
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
                (ChildSyncListener) any(),
                eq(postData), eq(requestContext));
    }    
    
}
