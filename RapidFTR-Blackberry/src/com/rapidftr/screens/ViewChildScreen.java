package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

import com.rapidftr.model.Child;

public class ViewChildScreen extends CustomScreen {

	Child child;
	public ViewChildScreen() {
	}

	public void setChild(Child child) {
		this.child = child;
		clearFields();
		renderChildFields(child);
	}

	private void renderChildFields(Child child) {

		Hashtable data = child.getKeyMap();
		for (Enumeration keyList = data.keys(); keyList.hasMoreElements();) {
			Object key = keyList.nextElement();
			Object value = data.get(key);

			add(new LabelField(key + ": " + value));
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
	
	protected void makeMenu(Menu menu, int instance) {
		MenuItem editChildMenu = new MenuItem("Edit Child Detail", 1, 1) {
			public void run() {
				controller.dispatcher().editChild(child);
			}
		};
		menu.add(editChildMenu);
	}
}
