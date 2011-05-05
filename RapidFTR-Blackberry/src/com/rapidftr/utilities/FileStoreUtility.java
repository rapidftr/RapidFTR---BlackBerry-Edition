package com.rapidftr.utilities;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class FileStoreUtility {

    private static final String FILE_STORE_HOME_USER = "file:///store/home/user";
    
    public static String getStorePath() {
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
