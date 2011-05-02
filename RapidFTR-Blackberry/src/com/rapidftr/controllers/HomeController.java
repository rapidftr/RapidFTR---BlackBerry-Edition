package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.Settings;
import net.rim.blackberry.api.browser.Browser;

public class HomeController extends Controller {
    private Settings settings;

    public HomeController(CustomScreen homeSreen, UiStack uiStack, Settings settings, Dispatcher dispatcher) {
        super(homeSreen, uiStack);
        this.settings = settings;
        this.dispatcher = dispatcher;
    }

    public void viewChildren() {
        dispatcher.viewChildren();
    }

    public void newChild() {
        dispatcher.newChild();
    }

    public void synchronize() {
        dispatcher.synchronize();
    }

    public void showSearch() {
        dispatcher.searchChild();
    }

    public void cleanAll() {
        dispatcher.resetDevice();
    }

    public void logIn() {
        dispatcher.login(null);
    }

    public void showcontact() {
        dispatcher.showcontact();
    }

    public void updateApplication() {
        Browser.getDefaultSession().displayPage(settings.getApplicationUpdateUrl());
    }
}
