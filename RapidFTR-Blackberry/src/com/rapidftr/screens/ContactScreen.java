package com.rapidftr.screens;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.screens.internal.CustomScreen;

public class ContactScreen extends CustomScreen {
	public ContactScreen() {
		showInformation();
	}

	public void showInformation() {
		addInformation("Name");
		addInformation("Position");
		addInformation("Organization");
		addInformation("Email");
		addInformation("Phone");
		addInformation("Location");
		addInformation("Other Information");
	}

	private void addInformation(String name) {
		add(new LabelField(name));
		add(new SeparatorField());
	}
}
