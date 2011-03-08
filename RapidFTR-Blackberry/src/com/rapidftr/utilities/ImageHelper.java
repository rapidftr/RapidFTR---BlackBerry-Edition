package com.rapidftr.utilities;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.util.Persistable;

public class ImageHelper implements Persistable {

	private int scaledWidth;
	private int scaledHeight;

	public Bitmap getScaledImage(String imageLocation, int xSize, int ySize) {
		return getScaledImage(xSize, ySize, "res/head.png", imageLocation, true);
	}

	public Bitmap getThumbnail(String imageLocation) {
		return getScaledImage(60, 60, "res/thumb.png", imageLocation, false);
	}

	private Bitmap getScaledImage(int width, int height, String defaultImage,
			String imageLocation, boolean maintainAspectRatio) {
		EncodedImage fullSizeImage = ImageUtility
				.getBitmapImageForPath(imageLocation);
		if (fullSizeImage == null) {
			return Bitmap.getBitmapResource(defaultImage);
		}
		
		  int imageWidth = fullSizeImage.getWidth(); 
		  int imageHeight = fullSizeImage.getHeight(); 
		  scaledWidth = width; 
		  scaledHeight = height;		 
		if (maintainAspectRatio) {
			calculateScaledDimensions(width, height, imageWidth, imageHeight);
		}
		Bitmap bitmap;
		int requiredWidth = Fixed32.toFP(scaledWidth);
		int requiredHeight = Fixed32.toFP(scaledHeight);
		bitmap = ImageUtility.scaleImage(fullSizeImage, requiredWidth,
				requiredHeight);
		return bitmap;
	}

	private void calculateScaledDimensions(int width, int height, int imageWidth,
			int imageHeight) {
		if (imageWidth >= imageHeight) {
			scaledWidth = (int) (imageWidth * (((float) height) / imageHeight));
			scaledHeight = height;
		} else {
			scaledHeight = (int) (imageHeight * (((float) width) / imageWidth));
			scaledWidth = width;
		}
	}

	public Bitmap getImage(String imageLocation) {
		return getScaledImage(300, 300, "res/head.png", imageLocation, false);
	}

}
