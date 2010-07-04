package com.rapidftr.screens;

import com.rapidftr.controllers.ApplicationRootController;
import com.rapidftr.controls.Button;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class ApplicationRootScreen extends MainScreen{

    private static final XYEdges PADDING = new XYEdges(16, 16, 16, 16);
    private ApplicationRootController controller;

    public ApplicationRootScreen() {
        super();
        layoutScreen();
    }

    private void layoutScreen() {
        Button newChildButton = new Button("New child", 200);
        newChildButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onNewChildClicked();
			}
		});
        Button viewChildrenButton = new Button("View all children", 200);
        viewChildrenButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onViewChildrenClicked();
			}
		});
        Button searchButton = new Button("Search for a child", 200);
        searchButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onSearchClicked();
			}
		});
        Button loginButton = new Button("Login", 200);
        loginButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onLoginClicked();
			}
		});
        Manager manager = new VerticalFieldManager(FIELD_HCENTER);
		manager.setPadding(PADDING);
		newChildButton.setPadding(PADDING);
		manager.add(newChildButton);
		searchButton.setPadding(PADDING);
		manager.add(viewChildrenButton);
		viewChildrenButton.setPadding(PADDING);
		manager.add(searchButton);
		loginButton.setPadding(PADDING);
		manager.add(loginButton);
		add(manager);
    }

    private void onViewChildrenClicked() {
        controller.viewChildren();
    }

    private void onLoginClicked() {
        controller.login();
    }

    private void onSearchClicked() {
    }

    private void onNewChildClicked() {
    }

    public void setApplicationRootController(ApplicationRootController controller) {
        this.controller = controller;
    }
}
