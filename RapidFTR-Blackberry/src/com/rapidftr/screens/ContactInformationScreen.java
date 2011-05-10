package com.rapidftr.screens;

import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.controllers.ContactInformationController;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.services.ScreenCallBack;

public class ContactInformationScreen extends CustomScreen implements
		ScreenCallBack {
	private final ContactInformation info;
	
	public ContactInformationScreen(ContactInformation info) {
		this.info = info;
	}

	public void setUp() {
		clearFields();
		((ContactInformationController) this.controller)
				.fetchContactInformation();
	}

	private void showInformation() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				try {
					addInformation("If you experience any problems with RapidFTR, " +
							"or believe your password has been exposed, please contact " +
							"the system administrator immediately");
					addInformation("Name\t\t: " + info.getName());
					addInformation("Position\t\t: " + info.getPosition());
					addInformation("Organization\t: " + info.getOrganization());
					addInformation("Email\t\t: " + info.getEmail());
					addInformation("Phone\t\t: " + info.getPhone());
					addInformation("Location\t\t: " + info.getLocation());
					addInformation("Other Information: " + info.getOther());
				} catch (Exception ignore) {
					Dialog.inform(ignore.getMessage());
				}
			}
		});
	}

	private void addInformation(String name) {
		add(new LabelField(name, FOCUSABLE));
		add(new SeparatorField());
	}

	public void onAuthenticationFailure() {
		// TODO Auto-generated method stub

	}

	public void onConnectionProblem() {
		// TODO Auto-generated method stub

	}

	public void onProcessFail(String failureMessage) {
		showInformation();
	}

	public void onProcessSuccess() {
		showInformation();
	}

	public void setProgressMessage(String message) {
		// TODO Auto-generated method stub

	}

	public void updateProgress(int progress) {
		// TODO Auto-generated method stub

	}
}
