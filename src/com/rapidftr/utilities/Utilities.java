package com.rapidftr.utilities;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiEngine;

import com.rapidftr.screens.HomeScreen;

public class Utilities {
	public static Bitmap getScaledBitmap(String name, int height) {
		EncodedImage ei = EncodedImage.getEncodedImageResource(name);

		return getScaledBitmap(ei, height);
	}

	public static Bitmap getScaledBitmapFromBytes(byte[] bytes, int height) {
		return getScaledBitmap(getEncodedImageFromBytes(bytes), height);
	}

	public static EncodedImage getScaledImage(String name, int height) {
		EncodedImage ei = EncodedImage.getEncodedImageResource(name);

		return getScaledImage(ei, height);
	}

	public static EncodedImage getScaledImage(EncodedImage encodedImage,
			int height) {
		int numerator = Fixed32.toFP(encodedImage.getHeight());
		int denominator = Fixed32.toFP(height);
		int heightScale = Fixed32.div(numerator, denominator);

		return encodedImage.scaleImage32(heightScale, heightScale);
	}

	public static Bitmap getScaledBitmap(EncodedImage encodedImage, int height) {
		EncodedImage newEi = getScaledImage(encodedImage, height);

		return newEi.getBitmap();
	}

	public static Bitmap getScaledBitmap(String name) {
		return getScaledBitmap(name, 40);
	}

	public static String pad(String str, int len) {
		System.out.println("Pad " + str + " len " + len);

		String output = str;

		if (str.length() < len) {
			int prefixLen = (len - str.length()) / 2;

			for (int i = 0; i < prefixLen; i++) {
				output = " " + output;
			}

			int suffixLen = len - str.length() - prefixLen;

			for (int i = 0; i < suffixLen; i++) {
				output = output + " ";
			}
		}

		System.out.println("padded [" + output + "]");
		return output;
	}

	/**
	 * Pop to Home Screen
	 */
	public static void popToHomeScreen(Screen screen) {
		UiEngine engine = screen.getUiEngine();

		Screen nextScreen = screen;
		Screen parentScreen;

		while ((parentScreen = nextScreen.getScreenBelow()) != null) {
			if (!(nextScreen instanceof HomeScreen)) {
				engine.popScreen(nextScreen);
			}

			nextScreen = parentScreen;
		}
	}

	public static EncodedImage getEncodedImageFromBytes(byte[] bytes) {
		return EncodedImage.createEncodedImage(bytes, 0, bytes.length);
	}

	public static Bitmap getBitmapFromBytes(byte[] bytes) {
		return (getEncodedImageFromBytes(bytes)).getBitmap();
	}

	public static byte[] getImageAsBytes(String imageName) {
		// Creates an EncodedImage from provided name resource
		EncodedImage image = EncodedImage.getEncodedImageResource(imageName);

		// Returns a byte array containing the encoded data for this
		// EncodedImage
		return image.getData();
	}
}
