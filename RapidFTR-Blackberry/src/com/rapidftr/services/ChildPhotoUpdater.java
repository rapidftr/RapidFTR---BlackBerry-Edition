package com.rapidftr.services;

import com.rapidftr.model.Child;
import com.rapidftr.net.HttpBatchRequestHandler;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.OutputStream;

public class ChildPhotoUpdater {

    private static final String FILE_STORE_HOME_USER = "file:///store/home/user";

    public void updateChildPhoto(Child child, HttpBatchRequestHandler requestHandler) {
        try {
            Arg[] httpArgs = new Arg[1];
            httpArgs[0] = HttpUtility.HEADER_CONTENT_TYPE_IMAGE;
            Response response = requestHandler.get("children/"
                    + child.getField("_id") + "/resized_photo/400", null,
                    httpArgs);
            byte[] data = response.getResult().getData();

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

            child.setField("current_photo_key", imagePath);
        } catch (IOException e) {
            child.syncFailed(e.getMessage());
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
}
