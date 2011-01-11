package com.rapidftr.model;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controls.Button;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.ImageUtility;

public class PhotoUploadFormField extends FormField implements
		ImageCaptureListener {

	private Bitmap bitmap;
	private VerticalFieldManager manager;
	private String imageLocation= "";
	private Button capturePhoto;
	private Child child;
	
	public PhotoUploadFormField(String name, String displayName, String type) {
		super(name, displayName, type);
	}

	public void initializeLayout(final ManageChildScreen newChildScreen) {

		final ImageCaptureListener imageChanageListener = this;
		child = newChildScreen.getChild();
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		if(child == null || child.getField("current_photo_key")=="")
		{
			bitmap = Bitmap.getBitmapResource("res/head.png");
		}
		else
		{
			bitmap = child.getImage();
		}
		capturePhoto = new Button(bitmap);

		capturePhoto.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				newChildScreen.takePhoto(imageChanageListener);
			}
		});

		manager.add(new LabelField(displayLabel()));
		manager.add(capturePhoto);

	}

	public Manager getLayout() {
		return manager;
	}

	//TODO: re render the field in the manage screen onExposed instead of doing it this way
	public void onImagedSaved(String imageLocation, EncodedImage encodedImage) {

		this.imageLocation = "file://" + imageLocation;


		int requiredWidth = Fixed32.toFP(bitmap.getWidth());
		int requiredHeight = Fixed32.toFP(bitmap.getHeight());

		bitmap = ImageUtility.scaleImage(encodedImage, requiredWidth,
				requiredHeight);

		capturePhoto.setBitmap(bitmap);

	}

	public String getValue() {
		return imageLocation;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!(obj instanceof PhotoUploadFormField))
			return false;

		PhotoUploadFormField field = (PhotoUploadFormField) obj;
		return name.equals(field.name);
	}

	public void setValue(String value) {
		imageLocation = value;		
	}

    public boolean isEmpty() {
        if (imageLocation == "") {
            return true;
        } else {
            return false;
        }
    }
}
