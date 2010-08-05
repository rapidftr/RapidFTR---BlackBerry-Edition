package com.rapidftr.utilities;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class FileUtility {

	public static byte[] getByteArray(String filename) {
		FileConnection fconn;
		try {
			fconn = (FileConnection) Connector.open("file://" + filename);

			InputStream input = fconn.openInputStream();
			byte[] data = new byte[(int) fconn.fileSize()];
			input.read(data, 0, data.length);
			return data;
		} catch (IOException e) {

			return null;
		}
	}

}
