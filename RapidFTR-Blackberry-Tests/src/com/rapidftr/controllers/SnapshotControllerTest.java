package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;
import net.rim.device.api.system.EncodedImage;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SnapshotControllerTest {

    private SnapshotScreen screen;
    private UiStack uiStack;
    private SnapshotController snapshotController;
    private ImageCaptureListener imageListener;
    private Dispatcher dispatcher;

    @Before
    public void setUp() {
        screen = mock(SnapshotScreen.class);
        uiStack = mock(UiStack.class);
        dispatcher = mock(Dispatcher.class);
        imageListener = mock(ImageCaptureListener.class);

        snapshotController = new SnapshotController(screen, uiStack, dispatcher, imageListener);
    }

    @Test
    public void shouldInvokImageCapturedActionOnImageListener() {
        String imageLocation = "image";
        EncodedImage encodedImage = mock(EncodedImage.class);
        snapshotController.capturedImage(imageLocation, encodedImage);
        verify(imageListener).onImagedSaved(imageLocation, encodedImage);
    }

}
