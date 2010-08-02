package com.rapidftr.screens;

import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controls.Button;

public class HomeScreen extends CustomScreen {

	private static final XYEdges PADDING = new XYEdges(10, 10, 10, 10);
	private Button searchButton;

	public HomeScreen() {
		layoutScreen();
	}

	private void layoutScreen() {

		Button newChildButton = new Button("New child");
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
		// Button loginButton = new Button("Login");
		// loginButton.setChangeListener(new FieldChangeListener() {
		// public void fieldChanged(Field field, int context) {
		// onLoginClicked();
		// }
		// });

		Button syncFormsButton = new Button("Sync Forms");
		syncFormsButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSyncFormsClicked();
			}
		});

		Vector buttonGroup = new Vector();
		buttonGroup.addElement(newChildButton);
		buttonGroup.addElement(viewChildrenButton);
		buttonGroup.addElement(searchButton);
		buttonGroup.addElement(syncFormsButton);
		// buttonGroup.addElement(loginButton);

		Button.setOptimimWidthForButtonGroup(buttonGroup);

		VerticalFieldManager manager = new VerticalFieldManager(FIELD_HCENTER);
		manager.setPadding(PADDING);

		newChildButton.setPadding(PADDING);
		manager.add(newChildButton);

		viewChildrenButton.setPadding(PADDING);
		manager.add(viewChildrenButton);
		searchButton.setPadding(PADDING);
		manager.add(searchButton);

		// loginButton.setPadding(PADDING);
		// manager.add(loginButton);

		syncFormsButton.setPadding(PADDING);
		manager.add(syncFormsButton);

		add(manager);

	}

	private void onViewChildrenClicked() {
		((HomeScreenController) controller).viewChildren();
	}

	// private void onLoginClicked() {
	// ((HomeScreenController) controller).login();
	// }

	private void onSearchClicked() {
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

}
