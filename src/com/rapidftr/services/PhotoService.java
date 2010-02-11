package com.rapidftr.services;

import com.rapidftr.utilities.Utilities;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;

public class PhotoService {
	private static final String IMAGE_NAME = "img/cait.jpg";
	
	public Bitmap getPhoto() {
		return getImage(IMAGE_NAME);
	}
	
	private Bitmap getImage(String imageName) {
		int imageHeight = Display.getHeight() - 60;

		return Utilities.getScaledBitmap(imageName, imageHeight);
	}
}
