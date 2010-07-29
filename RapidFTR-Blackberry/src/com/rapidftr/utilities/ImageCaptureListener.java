package com.rapidftr.utilities;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

public interface ImageCaptureListener {
	
	void onImagedSaved(String imageLocation,EncodedImage encodedImage);

}
