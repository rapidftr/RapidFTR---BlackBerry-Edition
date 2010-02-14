package com.rapidftr.services.impl;

import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

import com.rapidftr.services.PhotoService;
import com.rapidftr.services.RecordService;
import com.rapidftr.utilities.Utilities;

public class PhotoServiceImpl implements PhotoService {
	private static final String IMAGE_NAME = "img/cait.jpg";
	
	private static PhotoService instance;
	
	public static synchronized PhotoService getInstance() {
		if (instance == null) {
			instance = new PhotoServiceImpl();
		}

		return instance;
	}
	
	private PhotoServiceImpl() {}
	
	public EncodedImage getPhoto() {
		return getImage(IMAGE_NAME);
	}
	
	private EncodedImage getImage(String imageName) {
		int imageHeight = Display.getHeight() - 60;

		return Utilities.getScaledImage(imageName, imageHeight);
	}
}
