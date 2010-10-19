package com.rapidftr.utilities;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.system.EncodedImage;

public class ImageEncoder {
	
	public EncodedImage getEncodedImage(String path) throws IOException {
		FileConnection fconn = (FileConnection) Connector
				.open("file://" + path);

		InputStream input = fconn.openInputStream();
		byte[] data = new byte[(int) fconn.fileSize()];
		input.read(data, 0, data.length);

		return EncodedImage.createEncodedImage(data, 0, data.length);
	}

}
