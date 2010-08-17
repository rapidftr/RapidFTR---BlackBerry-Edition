package com.rapidftr.utilities;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

public class ImageUtility {

	public Bitmap scaleImage(EncodedImage encodedImage, int requiredWidth,
			int requiredHeight) {
		int currentWidth = Fixed32.toFP(encodedImage.getWidth());
		int currentHeight = Fixed32.toFP(encodedImage.getHeight());

		int scaleXFixed32 = Fixed32.div(currentWidth, requiredWidth);
		int scaleYFixed32 = Fixed32.div(currentHeight, requiredHeight);

		EncodedImage image = encodedImage.scaleImage32(scaleXFixed32,
				scaleYFixed32);
		
		return image.getBitmap();
	}

}
