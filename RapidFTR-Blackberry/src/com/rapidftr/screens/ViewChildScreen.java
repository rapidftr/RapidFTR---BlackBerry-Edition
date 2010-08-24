package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.ui.component.LabelField;

import com.rapidftr.model.Child;

public class ViewChildScreen extends CustomScreen {

	public ViewChildScreen() {
	}

	public void setChild(Child child) {
		clearFields();
		renderChildFields(child);
	}

	private void renderChildFields(Child child) {

		Hashtable data = child.getKeyMap();
		for (Enumeration keyList = data.keys(); keyList.hasMoreElements();) {
			Object key = keyList.nextElement();
			Object value = data.get(key);

			add(new LabelField(key + ": " + value, LabelField.FOCUSABLE));
		}

	}

	private void clearFields() {
		int fieldCount = this.getFieldCount();
		if (fieldCount > 0)
			this.deleteRange(0, fieldCount);
	}

	public void setUp() {
		// TODO Auto-generated method stub

	}

	public void cleanUp() {
		// TODO Auto-generated method stub

	}
}
