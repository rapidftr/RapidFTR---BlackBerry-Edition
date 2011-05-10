package com.rapidftr.utilities;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class AudioStore {

    private static final String FORMAT_EXT = ".amr";

    private String audioFilePath;
    private FileConnection fileConnection;
    private OutputStream outputStream;

    public OutputStream getOutputStream() throws IOException {
        String path = FileStoreUtility.getStorePath() + "/documents";
        createDirectoryIfNotExists(path);
        audioFilePath = generateLocation(path);
        fileConnection = (FileConnection) Connector.open(audioFilePath, Connector.READ_WRITE);
        if (!fileConnection.exists())
            fileConnection.create();
        outputStream = fileConnection.openDataOutputStream();
        return outputStream;
    }

    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.flush();
            outputStream.close();
        }
        if (fileConnection != null) {
            fileConnection.close();
        }
    }


    private void createDirectoryIfNotExists(String path) throws IOException {
        FileConnection directory = (FileConnection) Connector.open(path);
        if (!(directory).exists()) {
            directory.mkdir();
        }
    }

    private String generateLocation(String directory) {
        return directory + "/audio" + new Date().getTime() + FORMAT_EXT;
    }

    public String getFilePath() {
        return audioFilePath;
    }
}
