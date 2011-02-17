package com.rapidftr.services;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.RequestCallBack;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

public class ChildPhotoUpdateListener implements RequestListener {
    private static final String FILE_STORE_HOME_USER = "file:///store/home/user";
    private RequestCallBack requestCallBack;
    private int total;
    private boolean hasError;
    private ChildrenRecordStore childrenStore;

    public ChildPhotoUpdateListener(RequestCallBack requestCallBack, int total, boolean currentSyncStatus, ChildrenRecordStore childrenStore) {
        this.requestCallBack = requestCallBack;
        this.total = total;
        hasError = currentSyncStatus;
        this.childrenStore = childrenStore;
    }

    public void done(Object context, Response response) throws Exception {
        Child child = (Child) (((Hashtable) context).get(ChildSyncService.CHILD_TO_SYNC));
        requestCallBack.updateProgressMessage(
                            ((Hashtable) context).get(ChildSyncService.PROCESS_STATE).toString());
        try {
            byte[] data = response.getResult().getData();
            child.setPhotoKey(savePhoto(child, data));
        } catch (Exception e) {
            hasError = true;
            child.syncFailed(e.getMessage());
        }
        childrenStore.addOrUpdate(child);
        checkIfDone();
    }

    protected String savePhoto(Child child, byte[] data) throws IOException {
        String imagePath = getStorePath() + "/pictures/"
                + child.getField("current_photo_key") + ".jpg";
        FileConnection fc = (FileConnection) Connector.open(imagePath);
        if (!fc.exists()) {
            fc.create(); // create the file if it doesn't exist
        }
        fc.setWritable(true);
        OutputStream outStream = fc.openOutputStream();
        outStream.write(data);
        outStream.close();
        fc.close();

        return imagePath;
    }

    private synchronized void checkIfDone() {
        total--;
        if (total == 0) {
            if (hasError) {
                requestCallBack.onProcessFail("Errors have occurred");
            } else {
                requestCallBack.onProcessSuccess();
            }
        }
    }

    private String getStorePath() {
        String storePath = "";
        try {
            String sdCardPath = "file:///SDCard/Blackberry";
            FileConnection fc = (FileConnection) Connector.open(sdCardPath);
            if (fc.exists())
                storePath = sdCardPath;
            else
                storePath = FILE_STORE_HOME_USER;
        } catch (IOException ex) {
            storePath = FILE_STORE_HOME_USER;
        }
        return storePath;
    }

    public void readProgress(Object context, int bytes, int total) {
    }

    public void writeProgress(Object context, int bytes, int total) {
    }


}
