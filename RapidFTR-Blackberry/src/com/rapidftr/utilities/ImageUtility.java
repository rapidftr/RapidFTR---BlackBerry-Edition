package com.rapidftr.utilities;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;

public class ImageUtility {

	public static Bitmap scaleImage(EncodedImage encodedImage,
			int requiredWidth, int requiredHeight) {
		int currentWidth = Fixed32.toFP(encodedImage.getWidth());
		int currentHeight = Fixed32.toFP(encodedImage.getHeight());

		int scaleXFixed32 = Fixed32.div(currentWidth, requiredWidth);
		int scaleYFixed32 = Fixed32.div(currentHeight, requiredHeight);

		EncodedImage image = encodedImage.scaleImage32(scaleXFixed32,
				scaleYFixed32);

		return image.getBitmap();
	}

	public static Bitmap resizeBitmap(Bitmap image, int width, int height) {
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();

		// Need an array (for RGB, with the size of original image)
		int rgb[] = new int[imageWidth * imageHeight];

		// Get the RGB array of image into "rgb"
		image.getARGB(rgb, 0, imageWidth, 0, 0, imageWidth, imageHeight);

		// Call to our function and obtain rgb2
		int rgb2[] = rescaleArray(rgb, imageWidth, imageHeight, width, height);

		// Create an image with that RGB array
		Bitmap temp2 = new Bitmap(width, height);

		temp2.setARGB(rgb2, 0, width, 0, 0, width, height);

		return temp2;
	}

	private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2) {
		int out[] = new int[x2 * y2];
		for (int yy = 0; yy < y2; yy++) {
			int dy = yy * y / y2;
			for (int xx = 0; xx < x2; xx++) {
				int dx = xx * x / x2;
				out[(x2 * yy) + xx] = ini[(x * dy) + dx];
			}
		}
		return out;
	}
	
	public static EncodedImage getBitmapImageForPath(String Path)
	{
	//	String ImagePath = "file://"+ Path;
	    String ImagePath = Path;
		
	FileConnection fconn;

	try {
		fconn = (FileConnection) Connector.open(ImagePath,
				Connector.READ);
		if (fconn.exists()) {
			byte[] imageBytes = new byte[(int) fconn.fileSize()];
			InputStream inStream = fconn.openInputStream();
			inStream.read(imageBytes);
			inStream.close();
			EncodedImage eimg= EncodedImage.createEncodedImage(
					imageBytes, 0, (int) fconn.fileSize());
			fconn.close();
			return eimg;

		}

	}catch (IllegalArgumentException e)
	{
		return  EncodedImage.getEncodedImageResource("res\\head.png") ;
	}
	catch (IOException e) {
		e.printStackTrace();
		return  EncodedImage.getEncodedImageResource("res\\head.png") ;
	}
	return null;
	}
}
