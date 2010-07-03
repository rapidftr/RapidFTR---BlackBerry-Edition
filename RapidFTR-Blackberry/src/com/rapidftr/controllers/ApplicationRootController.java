package com.rapidftr.controllers;

import com.rapidftr.screens.ApplicationRootScreen;
import com.rapidftr.screens.UiStack;

public class ApplicationRootController {
    private LoginController loginController;
    private ApplicationRootScreen applicationRootScreen;
    private UiStack uiStack;

    public ApplicationRootController(LoginController loginController, ApplicationRootScreen applicationRootScreen, UiStack uiStack) {
        this.loginController = loginController;
        this.applicationRootScreen = applicationRootScreen;
        this.uiStack = uiStack;
        applicationRootScreen.setApplicationRootController(this);
    }

    public void start() {
        uiStack.pushScreen(applicationRootScreen);
    }

    public void login() {
        loginController.show();
    }               
}
