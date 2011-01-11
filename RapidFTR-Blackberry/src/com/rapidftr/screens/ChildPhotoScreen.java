package com.rapidftr.screens;

import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;

public class ChildPhotoScreen extends CustomScreen {

	private LabelField childName;

	public void setChild(Child child) {
		clearFields();
		childName = new LabelField(child.getField("name"));
		add(childName);
		add(new SeparatorField());
		add(new BitmapField(child.getImage()));
	}

}
