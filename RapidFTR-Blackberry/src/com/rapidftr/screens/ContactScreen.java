package com.rapidftr.screens;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.screens.internal.CustomScreen;

public class ContactScreen extends CustomScreen {
	private final ContactInformation info;

	public ContactScreen(ContactInformation info) {
		this.info = info;
		showInformation();
	}

	public void showInformation() {
		addInformation("Name\t\t: " + info.getName());
		addInformation("Position\t\t: " + info.getPosition());
		addInformation("Organization\t: " + info.getOrganization());
		addInformation("Email\t\t: " + info.getEmail());
		addInformation("Phone\t\t: " + info.getPhone());
		addInformation("Location\t\t: " + info.getLocation());
		addInformation("Other Information: " + info.getOther());
	}

	private void addInformation(String name) {
		add(new LabelField(name));
		add(new SeparatorField());
	}
}
