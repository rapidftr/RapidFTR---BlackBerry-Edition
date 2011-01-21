package com.rapidftr.screens;

import java.util.Vector;

import com.rapidftr.Main;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controllers.HomeController;
import com.rapidftr.controls.Button;
import com.rapidftr.net.ConnectionFactory;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.Settings;

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
		((HomeController) controller).logIn();
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
            ((HomeController) controller).synchronize();
        }
    }

	private void onViewChildrenClicked() {
		((HomeController) controller).viewChildren();
	}

	private void onSearchClicked() {
		((HomeController) controller).showSearch();
	}

	private void onNewChildClicked() {
		((HomeController) controller).newChild();
	}

    private void onCleanDeviceClicked() {
        int result = Dialog
                .ask(
                        Dialog.D_YES_NO,
                        "Do you want to clean the device? This will clear all the locally stored child records and login information");
        if (result == Dialog.YES) {
            ((HomeController) controller).cleanAll();
            Dialog.alert("Device successfully cleaned");
            onLoginButtonClicked();
        }
    }

    protected void makeMenu(Menu menu, int instance) {
        int userOptions = 1;
        int firstSeparator = 2;
        int advancedOptions = 3;
        MenuItem cleanDeviceMenuItem = new MenuItem("Clean Device", advancedOptions, 2) {
            public void run() {
                onCleanDeviceClicked();
            }
        };
        MenuItem syncInfoItem = new MenuItem("Last Sync Info", userOptions, 2) {
            public void run() {
                Dialog.alert(settings.getLastSyncInfo());
            }
        };
        MenuItem contactHelpItem = new MenuItem("Contact & Help", userOptions, 1) {
            public void run() {
                ((HomeController) controller).showcontact();
            }
        };
        MenuItem updateApplicationMenuItem = new MenuItem("Update RapidFTR", advancedOptions, 1) {
            public void run() {
                ((HomeController) controller).updateApplication();
            }
        };


        menu.add(contactHelpItem);
        menu.add(updateApplicationMenuItem);
        menu.add(MenuItem.separator(firstSeparator));
        menu.add(cleanDeviceMenuItem);
        menu.add(syncInfoItem);
    }


	protected void onExposed() {
		clearFields();
		layoutScreen();
		super.onExposed();
        if (!((Main)this.getApplication()).permissionsGranted) {
            onClose();
            System.exit(-1);
        }
	}
	
	public boolean keyChar( char key, int status, int time ) 
    {
		return false;
    }
    
    public boolean keyDown(int keycode, int time) 
    {
		return false;
    }

}
