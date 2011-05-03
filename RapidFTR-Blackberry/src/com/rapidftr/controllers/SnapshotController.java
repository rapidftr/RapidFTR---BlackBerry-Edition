package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;
import net.rim.device.api.system.EncodedImage;

public class SnapshotController extends Controller {

    private final ImageCaptureListener imageListener;

    public SnapshotController(SnapshotScreen screen, UiStack uiStack, Dispatcher dispatcher, ImageCaptureListener imageListener) {
        super(screen, uiStack, dispatcher);
        this.imageListener = imageListener;
    }

    public void capturedImage(String imageLocation, EncodedImage encodedImage) {
        imageListener.onImagedSaved(imageLocation, encodedImage);
    }
}
