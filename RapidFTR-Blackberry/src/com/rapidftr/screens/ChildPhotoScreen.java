package com.rapidftr.screens;

import com.rapidftr.utilities.ImageHelper;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.model.ScrollableImageField;

;

public class ChildPhotoScreen extends CustomScreen {

	private LabelField childName;
	private Child curChild;
	private int headerHeight = 0;
	private SeparatorField separatorField;

	public void setChild(Child child) {
		this.curChild = child;
		clearFields();
		childName = new LabelField(child.getField("name"));
		add(childName);
		separatorField = new SeparatorField();
		add(separatorField);
	}

	protected void onUiEngineAttached(boolean attached) {
		super.onUiEngineAttached(attached);
		setHeaderHeight();
		add(new ScrollableImageField(
				new ImageHelper().getScaledImage(
						curChild.getImageLocation(), 
						Display.getWidth(),
						Display.getHeight()-headerHeight),
				this.headerHeight)
		);
	}

	private void setHeaderHeight() {
		int newHeaderHeight = separatorField.getTop() + separatorField.getHeight();
		if (headerHeight != newHeaderHeight)
			headerHeight = newHeaderHeight;
	}

}
