package com.rapidftr.services;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.RequestCallBack;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import javax.microedition.io.HttpConnection;
import java.util.Hashtable;
import java.util.Vector;

public class ChildSyncListener implements RequestListener {

    private RequestCallBack requestCallBack;
    private int totalToUploadAndDownload;
    private Vector childrenRequiringPhotoUpdate;
    private ChildrenRecordStore childRecordStore;
    private ChildPhotoUpdater photoUpdater;
    private boolean hasError;

    public ChildSyncListener(RequestCallBack requestCallBack, int totalToUploadAndDownload,
                             ChildrenRecordStore childRecordStore, ChildPhotoUpdater photoUpdater) {
        this.requestCallBack = requestCallBack;
        this.totalToUploadAndDownload = totalToUploadAndDownload;
        this.childRecordStore = childRecordStore;
        this.photoUpdater = photoUpdater;
        this.hasError = false;
        this.childrenRequiringPhotoUpdate = new Vector();
    }

    public void done(Object context, Response response) throws Exception {
        Child child = (Child) (((Hashtable) context).get(ChildSyncService.CHILD_TO_SYNC));
        requestCallBack.updateProgressMessage(
                            ((Hashtable) context).get(ChildSyncService.PROCESS_STATE).toString());
        
        if (isValidResponse(response)) {
            try {
                JSONObject jsonChild = new JSONObject(response.getResult().toString());
                JSONArray fieldNames = jsonChild.names();
                for (int j = 0; j < fieldNames.length(); j++) {
                    String fieldName = fieldNames.get(j).toString();
                    String fieldValue = jsonChild.getString(fieldName);
                    child.setField(fieldName, fieldValue);
                }
                child.syncSuccess();
            } catch (Exception e) {
                e.printStackTrace();
                child.syncFailed(e.getMessage());
                hasError = true;
            }
        } else {
            hasError = true;
            child.syncFailed(response.getErrorMessage());
        }
        childRecordStore.addOrUpdate(child);
        checkIfDone(child);
    }

    private synchronized void checkIfDone(Child child) {
        if (child.hasPhoto()) {
            childrenRequiringPhotoUpdate.addElement(child);
        }
        totalToUploadAndDownload--;
        if (totalToUploadAndDownload == 0) {
            if (childrenRequiringPhotoUpdate.size() == 0) {
                if (hasError) {
                    requestCallBack.onProcessFail("Errors have occurred");
                } else {
                    requestCallBack.onProcessSuccess();
                }
            } else {
                photoUpdater.doUpdates(childrenRequiringPhotoUpdate, requestCallBack, hasError);
            }
        }
    }

    private boolean isValidResponse(Response response) {
        return (response.getException() == null)
                && (response.getCode() == HttpConnection.HTTP_OK || response
                .getCode() == HttpConnection.HTTP_CREATED);
    }

    public void readProgress(Object context, int bytes, int total) {
    }

    public void writeProgress(Object context, int bytes, int total) {
    }

    public int getTotal() {
        return totalToUploadAndDownload;
    }
}
