package com.rapidftr.controllers;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;

import com.rapidftr.controls.Button;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.screens.NewChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;

public class NewChildController extends Controller {


	private final FormStore formStore;

	public NewChildController(NewChildScreen screen, UiStack uiStack,
			FormStore formStore) {
		super(screen, uiStack);
		this.formStore = formStore;
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();
	}

	public void show() {
		((NewChildScreen) screen).setForms(formStore.getForms());

		super.show();
	}





	public void takeSnapshotAndUpdateWithNewImage(
			ImageCaptureListener imageCaptureListener) {

		SnapshotController snapshotController = new SnapshotController(
				new SnapshotScreen(), uiStack);
		snapshotController.show();
		snapshotController.setImageListener(imageCaptureListener);
	}

}
