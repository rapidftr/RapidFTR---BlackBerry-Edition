package com.rapidftr.screens;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controls.Button;
import com.rapidftr.controls.TitleField;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.SettingsStore;

public class HomeScreen extends CustomScreen {

	private static final XYEdges PADDING = new XYEdges(10, 10, 10, 10);
	private Button searchButton;
	private SettingsStore settings;

	public HomeScreen(SettingsStore settings) {
		this.settings = settings;
		layoutScreen();
	}

	private void layoutScreen() {

		Button loginButton;
		if (settings.isUserLoggedIn()) {
			loginButton = new Button("Log Out");
			loginButton.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					onLogOutButtonClicked();
				}
			});
		} else {
			loginButton = new Button("Log In");
			loginButton.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					onLoginButtonClicked();
				}
			});
		}

		Button newChildButton = new Button("Add New child");
		newChildButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onNewChildClicked();
			}
		});
		Button viewChildrenButton = new Button("View all children");
		viewChildrenButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onViewChildrenClicked();
			}
		});

		searchButton = new Button("Search for a child");
		searchButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSearchClicked();
			}
		});

		Button syncFormsButton = new Button("Sync Forms");
		syncFormsButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSyncFormsClicked();
			}
		});

		Button syncAllButton = new Button("Sync All");
		syncAllButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSyncAllClicked();
			}
		});

		Button cleanAllButton = new Button("Clean Device");
		cleanAllButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				int result = Dialog
						.ask(
								Dialog.D_YES_NO,
								"Do you want to clean the device? This will clear all the locally stored child records and login information");
				if (result == Dialog.YES) {
					((HomeScreenController) controller).cleanAll();
					Dialog.alert("Device successfully cleaned");
				}
			}
		});

		Vector buttonGroup = new Vector();
		buttonGroup.addElement(loginButton);
		buttonGroup.addElement(newChildButton);
		buttonGroup.addElement(viewChildrenButton);
		buttonGroup.addElement(searchButton);
		buttonGroup.addElement(syncFormsButton);
		buttonGroup.addElement(syncAllButton);
		buttonGroup.addElement(cleanAllButton);

		Button.setOptimimWidthForButtonGroup(buttonGroup);
		VerticalFieldManager manager = new VerticalFieldManager(FIELD_HCENTER);
		manager.setPadding(PADDING);
		loginButton.setPadding(PADDING);
		manager.add(loginButton);
		newChildButton.setPadding(PADDING);
		manager.add(newChildButton);
		viewChildrenButton.setPadding(PADDING);
		manager.add(viewChildrenButton);
		searchButton.setPadding(PADDING);
		manager.add(searchButton);
		// uploadChildRecordsButton.setPadding(PADDING);
		// manager.add(uploadChildRecordsButton);
		syncFormsButton.setPadding(PADDING);
		manager.add(syncFormsButton);
		syncAllButton.setPadding(PADDING);
		manager.add(syncAllButton);

		cleanAllButton.setPadding(PADDING);
		manager.add(cleanAllButton);

		add(manager);

	}

	protected void onLogOutButtonClicked() {
		int result = Dialog.ask(Dialog.D_YES_NO,
				"Are you sure you want to Log Out ?");
		if (result == Dialog.YES) {
			settings.clearState();
			Dialog.alert("Successfully Logged Out");
		}
	}

	protected void onLoginButtonClicked() {
		((HomeScreenController) controller).logIn();
	}

	protected void onSyncAllClicked() {
		((HomeScreenController) controller).syncAll();
	}

	private void onViewChildrenClicked() {
		((HomeScreenController) controller).viewChildren();
	}

	private void onSearchClicked() {
		((HomeScreenController) controller).showSearch();
	}

	private void onNewChildClicked() {
		((HomeScreenController) controller).newChild();
	}

	private void onSyncFormsClicked() {
		((HomeScreenController) controller).synchronizeForms();
	}

	public void setUp() {

	}

	public void cleanUp() {

	}

	protected void onExposed() {
		clearFields();
		layoutScreen();
		super.onExposed();
	}

}
