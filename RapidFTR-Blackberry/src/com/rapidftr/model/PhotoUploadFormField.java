package com.rapidftr.model;

import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.LabelField;

import com.rapidftr.controls.Button;
import com.rapidftr.form.FormField;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.ImageHelper;
import com.rapidftr.utilities.ImageUtility;

public class PhotoUploadFormField extends CustomField implements
		ImageCaptureListener {

	private Bitmap bitmap;
	private Button capturePhoto;
	private String imageLocation;

	public PhotoUploadFormField(FormField field) {
		super(Field.FIELD_LEFT);
		final ImageCaptureListener imageChanageListener = this;
		bitmap = createBitmap("res/head.png");
		capturePhoto = new Button(bitmap);
		capturePhoto.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				getChildScreen().takePhoto(imageChanageListener);
			}
		});
		add(new LabelField(field.getDisplayName()));
		add(capturePhoto);

	}

	// TODO: re render the field in the manage screen onExposed instead of doing
	// it this way
	public void onImagedSaved(String imageLocation, EncodedImage encodedImage) {
		this.imageLocation = "file://" + imageLocation;
		int requiredWidth = Fixed32.toFP(bitmap.getWidth());
		int requiredHeight = Fixed32.toFP(bitmap.getHeight());
		bitmap = ImageUtility.scaleImage(encodedImage, requiredWidth, requiredHeight);
		capturePhoto.setBitmap(bitmap);
	}

	protected void onDisplay() {
		drawChildImage();
		super.onDisplay();
	}

	private void drawChildImage() {
		Child child = getChildScreen().getChild();
		if (child != null) {
			setButtonBackground(getChildImage(child));
		}
	}

	private Bitmap getChildImage(Child child) {
		return new ImageHelper().getImage(child.getImageLocation());
	}

	private void setButtonBackground(Bitmap bitmap) {
		capturePhoto.setBitmap(bitmap);
	}

	private Bitmap createBitmap(String location) {
		return Bitmap.getBitmapResource(location);
	}

	private ManageChildScreen getChildScreen() {
		return (ManageChildScreen) getScreen();
	}
	
	public void setValue(String value) {
		imageLocation = value;		
	}
}
