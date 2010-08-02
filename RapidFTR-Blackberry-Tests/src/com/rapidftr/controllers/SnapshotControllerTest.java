package com.rapidftr.controllers;

import net.rim.device.api.system.EncodedImage;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;

public class SnapshotControllerTest {

	private SnapshotScreen screen;
	private UiStack uiStack;
	private SnapshotController snapshotController;
	private ImageCaptureListener imageListener;

	@Before
	public void setUp() {
		screen = mock(SnapshotScreen.class);
		uiStack = mock(UiStack.class);
		snapshotController = new SnapshotController(screen, uiStack);
		imageListener = mock(ImageCaptureListener.class);
		snapshotController.setImageListener(imageListener);
	}

	@Test
	public void shouldInvokImageCapturedActionOnImageListener() {
		String imageLocation = "image";
		EncodedImage encodedImage = mock(EncodedImage.class);
		snapshotController.capturedImage(imageLocation, encodedImage);
		verify(imageListener).onImagedSaved(imageLocation, encodedImage);
	}

}
