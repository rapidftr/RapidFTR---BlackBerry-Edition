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
		addInformation("Name\t: " + info.getName());
		addInformation("Position\t: " + info.getPosition());
		addInformation("Organization\t: " + info.getOrganization());
		addInformation("Email\t: " + info.getEmail());
		addInformation("Phone\t: " + info.getPhone());
		addInformation("Location\t: " + info.getLocation());
		addInformation("Other Information\t: " + info.getOther());
	}

	private void addInformation(String name) {
		add(new LabelField(name));
		add(new SeparatorField());
	}
}
