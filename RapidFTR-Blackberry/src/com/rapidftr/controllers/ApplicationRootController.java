package com.rapidftr.controllers;

import com.rapidftr.screens.ApplicationRootScreen;
import com.rapidftr.screens.UiStack;

public class ApplicationRootController {
    private LoginController loginController;
    private ApplicationRootScreen applicationRootScreen;
    private UiStack uiStack;
    private final ViewChildrenController viewChildrenController;

    public ApplicationRootController(ApplicationRootScreen applicationRootScreen, UiStack uiStack, LoginController loginController, ViewChildrenController viewChildrenController) {
        this.loginController = loginController;
        this.applicationRootScreen = applicationRootScreen;
        this.uiStack = uiStack;
        this.viewChildrenController = viewChildrenController;
        applicationRootScreen.setApplicationRootController(this);
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
}
