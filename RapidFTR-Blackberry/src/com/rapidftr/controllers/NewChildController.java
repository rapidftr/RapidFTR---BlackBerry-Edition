package com.rapidftr.controllers;

import com.rapidftr.screens.NewChildScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.PhotoService;
import com.rapidftr.services.PhotoServiceListener;
import net.rim.device.api.system.EncodedImage;

public class NewChildController {

    private NewChildScreen newChildScreen;
    private UiStack  uiStack;
    private PhotoService photoService;

    public NewChildController(UiStack uiStack, NewChildScreen newChildScreen, PhotoService photoService) {
        this.uiStack = uiStack;
        this.newChildScreen = newChildScreen;
        this.photoService = photoService;
        newChildScreen.setNewChildController(this);
    }

    public void show() {
        uiStack.pushScreen(newChildScreen);
    }

    public void capturePhoto() {
        photoService.startCamera(new PhotoServiceListener() {
            public void handlePhoto(EncodedImage image) {
                photoCaptured(image);
            }
        });
    }

    private void photoCaptured(EncodedImage image) {
        newChildScreen.setImage(image);
    }
}
