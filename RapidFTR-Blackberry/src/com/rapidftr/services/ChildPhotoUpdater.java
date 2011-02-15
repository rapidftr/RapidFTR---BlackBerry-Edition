package com.rapidftr.services;

import com.rapidftr.model.Child;
import com.rapidftr.net.HttpBatchRequestHandler;
import com.rapidftr.utilities.FileStoreUtility;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.OutputStream;

public class ChildPhotoUpdater {

    public void updateChildPhoto(Child child, HttpBatchRequestHandler requestHandler) {
        try {
            Arg[] httpArgs = new Arg[1];
            httpArgs[0] = HttpUtility.HEADER_CONTENT_TYPE_IMAGE;
            Response response = requestHandler.get("children/"
                    + child.getField("_id") + "/resized_photo/400", null,
                    httpArgs);
            byte[] data = response.getResult().getData();

            String imagePath = FileStoreUtility.getStorePath() + "/pictures/"
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

}
