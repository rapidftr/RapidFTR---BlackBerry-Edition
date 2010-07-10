package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.ApplicationRootScreen;
import com.rapidftr.screens.UiStack;

public class ApplicationRootController {
    private LoginController loginController;
    private ApplicationRootScreen applicationRootScreen;
    private UiStack uiStack;
    private final ViewChildrenController viewChildrenController;
    private final NewChildController newChildController;
    private final ViewChildController viewChildController;

    public ApplicationRootController(ApplicationRootScreen applicationRootScreen, UiStack uiStack, LoginController loginController,
                                     ViewChildrenController viewChildrenController, NewChildController newChildController, ViewChildController viewChildController) {
        this.loginController = loginController;
        this.applicationRootScreen = applicationRootScreen;
        this.uiStack = uiStack;
        this.viewChildrenController = viewChildrenController;
        this.newChildController = newChildController;
        this.viewChildController = viewChildController;
        applicationRootScreen.setApplicationRootController(this);
        this.SetApplicationRootController();
    }

    public void start() {
        uiStack.pushScreen(applicationRootScreen);
    }

    public void login() {
        loginController.show();
    }

    public void viewChildren() {
        viewChildrenController.show();
    }

    public void newChildRecord() {
        newChildController.show();
    }

    public void viewChild(Child child) {
        viewChildController.show(child);
    }

    public void SetApplicationRootController() {
        viewChildrenController.setApplicationRootController(this);
    }
}
