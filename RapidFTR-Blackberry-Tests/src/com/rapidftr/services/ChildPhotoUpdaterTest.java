package com.rapidftr.services;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.MockStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.net.HttpService;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import org.junit.Test;

import java.util.Vector;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class ChildPhotoUpdaterTest {

    @Test
    public void shouldSendPhotoUpdateRequest() {
        ChildrenRecordStore store = new ChildrenRecordStore(new MockStore());
        HttpService httpService = mock(HttpService.class);
        RequestCallBack callback = mock(RequestCallBack.class);
        ChildPhotoUpdater photoUpdater = new ChildPhotoUpdater(httpService, store);
        Child child = ChildFactory.existingChild("id");
        child.setPhotoKey("somevalue");
        Vector children = new Vector();
        children.addElement(child);

        photoUpdater.doUpdates(children, callback, false);
        verify(httpService).get(eq("children/id/resized_photo/400"), eq((Arg[])null), eq(new Arg[] {HttpUtility.HEADER_CONTENT_TYPE_IMAGE}),
                (RequestListener) any(), any());
    }
}
