package com.rapidftr.screens;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.ImageUtility;

public class ChildPhotoScreen extends CustomScreen{

	private Child child;
	private LabelField childName ;
	
	public void setChild(Child child) {
		this.child = child;
		clearFields();
		childName=new LabelField(child.getField("name"));
		add(childName);
		add(new SeparatorField());
		renderChildFields(child);
	}
	private void renderChildFields(Child child) {
		
		EncodedImage eimage = ImageUtility.getBitmapImageForPath(((String) child.getField("current_photo_key")));
		Bitmap image = eimage.getBitmap();
		image = ImageUtility.resizeBitmap(image, Display.getWidth(),Display.getHeight()-childName.getHeight());
		BitmapField imageField= new BitmapField(image);
		add(imageField);
	}
	public void cleanUp() {
		
	}
	public void setUp() {
	}
}
