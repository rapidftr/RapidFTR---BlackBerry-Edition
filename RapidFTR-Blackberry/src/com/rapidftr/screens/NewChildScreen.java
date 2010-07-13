package com.rapidftr.screens;

import com.rapidftr.controllers.NewChildController;
import com.rapidftr.controls.Button;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;

public class NewChildScreen extends MainScreen {

    private NewChildController newChildController;
    private BitmapField imageField;

    public NewChildScreen() {
        layoutScreen();
    }

    private void layoutScreen() {
        Button capturePhotoButton = new Button("Capture photo", 200);
        capturePhotoButton.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int i) {
                onCapturePhotoClicked();
            }
        });
        imageField = new BitmapField();
        add(imageField);
        add(capturePhotoButton);
    }

    private void onCapturePhotoClicked() {
         newChildController.capturePhoto();
    }

    public void setNewChildController(NewChildController newChildController) {
        this.newChildController = newChildController;
    }

    public void setImage(EncodedImage image) {
        imageField.setBitmap(image.getBitmap());
    }
}
