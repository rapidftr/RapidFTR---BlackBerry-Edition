package com.rapidftr.services;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpService;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ChildPhotoUpdater {

    private HttpService service;
    private ChildrenRecordStore recordStore;

    public ChildPhotoUpdater(HttpService service, ChildrenRecordStore recordStore) {
        this.service = service;
        this.recordStore = recordStore;
    }

    public void doUpdates(Vector childrenRequiringPhotoUpdate, RequestCallBack requestCallBack, boolean currentSyncStatus) {
        int total = childrenRequiringPhotoUpdate.size();
        Enumeration items = childrenRequiringPhotoUpdate.elements();
		int index = 0;
        ChildPhotoUpdateListener listener = new ChildPhotoUpdateListener(requestCallBack, total, currentSyncStatus, recordStore);
		while (items.hasMoreElements()) {
            Child child = (Child) items.nextElement();
			index++;
			Hashtable context = new Hashtable();
			context.put(ChildSyncService.PROCESS_STATE, "Updating photo [" + index + "/"
					+ total + "]");
            context.put(ChildSyncService.CHILD_TO_SYNC, child);
            Arg[] httpArgs = new Arg[1];
            httpArgs[0] = HttpUtility.HEADER_CONTENT_TYPE_IMAGE;
            service.get("children/"
                    + child.getField("_id") + "/resized_photo/400", null,
                    httpArgs, listener, context);
        }
    }
}