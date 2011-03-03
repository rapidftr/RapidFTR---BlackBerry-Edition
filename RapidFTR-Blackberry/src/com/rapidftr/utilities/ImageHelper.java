package com.rapidftr.utilities;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.util.Persistable;

public class ImageHelper implements Persistable {

    public Bitmap getScaledImage(String imageLocation, int xSize, int ySize) {
        return getScaledImage(xSize, ySize, "res/head.png", imageLocation);
    }

    public Bitmap getThumbnail(String imageLocation) {
        return getScaledImage(60, 60, "res/thumb.png", imageLocation);
    }


    private Bitmap getScaledImage(int width, int height, String defaultImage, String imageLocation) {
        EncodedImage fullSizeImage = ImageUtility.getBitmapImageForPath(imageLocation);
        //fullSizeImage.getW

        Bitmap bitmap;
        if (fullSizeImage != null) {
            int requiredWidth = Fixed32.toFP(width);
            int requiredHeight = Fixed32.toFP(height);
            bitmap = ImageUtility.scaleImage(fullSizeImage,requiredWidth, requiredHeight);
        } else {
            bitmap = Bitmap.getBitmapResource(defaultImage);
        }

        return bitmap;
    }


}
