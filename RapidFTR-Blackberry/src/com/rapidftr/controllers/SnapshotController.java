package com.rapidftr.controllers;

import net.rim.device.api.system.EncodedImage;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;

public class SnapshotController extends Controller {

	private ImageCaptureListener imageListener;

	public SnapshotController(SnapshotScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}

	public void capturedImage(String imageLocation, EncodedImage encodedImage) {
		imageListener.onImagedSaved(imageLocation, encodedImage);
	}

	public void setImageListener(ImageCaptureListener imageListener) {
		this.imageListener = imageListener;
	}

}
