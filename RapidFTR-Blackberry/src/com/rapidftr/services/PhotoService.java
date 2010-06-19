package com.rapidftr.services;

import net.rim.device.api.system.EncodedImage;


public interface PhotoService {

	void startCamera(PhotoServiceListener listener);
	
	EncodedImage getPhoto();
}
