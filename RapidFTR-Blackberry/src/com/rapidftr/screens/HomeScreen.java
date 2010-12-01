package com.rapidftr.screens;

import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controls.Button;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.Settings;
import net.rim.device.api.ui.*;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;

import java.util.Vector;

public class HomeScreen extends CustomScreen {

	private static final XYEdges PADDING = new XYEdges(10, 10, 10, 10);
	private Button searchButton;
	private Settings settings;
	public HomeScreen(Settings settings) {
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

		Button newChildButton = new Button("Register Child");
		newChildButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onNewChildClicked();
			}
		});
		Button viewChildrenButton = new Button("View All Children");
		viewChildrenButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onViewChildrenClicked();
			}
		});

		searchButton = new Button("Search for a Child");
		searchButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSearchClicked();
			}
		});

        Button synchronizeButton = new Button("Synchronize");
        synchronizeButton.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                      onSynchronizeClicked();
            }
        });

        Vector buttonGroup = new Vector();
		buttonGroup.addElement(loginButton);
		buttonGroup.addElement(newChildButton);
		buttonGroup.addElement(viewChildrenButton);
		buttonGroup.addElement(searchButton);

		buttonGroup.addElement(synchronizeButton);

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

		synchronizeButton.setPadding(PADDING);
		manager.add(synchronizeButton);

		add(manager);

	}

	protected void onLogOutButtonClicked() {
		int result = Dialog.ask(Dialog.D_YES_NO,
				"Are you sure you want to Log Out ?");
		if (result == Dialog.YES) {
			settings.clearAuthenticationInfo();
			Dialog.alert("Successfully Logged Out");
		}
	}

	protected void onLoginButtonClicked() {
		((HomeScreenController) controller).logIn();
	}

    protected void onSynchronizeClicked() {
        if (ConnectionFactory.isNotConnected()) {
            Dialog.ask(Dialog.D_OK,
                    "Could not establish connection with host because all connectors are offline");
        } else if (!settings.isUserLoggedIn()) {
            int result = Dialog.ask(Dialog.D_OK_CANCEL,
                    "You are not logged in.\n Press ok to  login.");
            if (result == Dialog.OK) {
                onLoginButtonClicked();
            }
        } else {
            ((HomeScreenController) controller).synchronize();
        }
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

    private void onCleanDeviceClicked() {
        int result = Dialog
                .ask(
                        Dialog.D_YES_NO,
                        "Do you want to clean the device? This will clear all the locally stored child records and login information");
        if (result == Dialog.YES) {
            ((HomeScreenController) controller).cleanAll();
            Dialog.alert("Device successfully cleaned");
            onLoginButtonClicked();
        }
    }

    protected void makeMenu(Menu menu, int instance) {
        MenuItem cleanDeviceMenuItem = new MenuItem("Clean Device", 1, 1) {
			public void run() {
				onCleanDeviceClicked();
			}
		};
		menu.add(cleanDeviceMenuItem);
		
        MenuItem syncInfoItem = new MenuItem("Last Sync Info", 1, 1) {
			public void run() {
                Dialog.alert(settings.getLastSyncInfo());
			}
		};
		menu.add(syncInfoItem);
		//super.makeMenu(menu, instance);
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
