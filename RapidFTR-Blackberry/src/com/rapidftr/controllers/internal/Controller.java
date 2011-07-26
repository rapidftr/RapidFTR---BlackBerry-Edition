package com.rapidftr.controllers.internal;

import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

abstract public class Controller {

    protected Dispatcher dispatcher;
    protected CustomScreen currentScreen;
    protected UiStack uiStack;

    public Controller(CustomScreen screen, UiStack uiStack, Dispatcher dispatcher) {
        this.currentScreen = screen;
        this.uiStack = uiStack;
        this.currentScreen.setController(this);
        this.dispatcher = dispatcher;
    }

    public void show() {
        if (!currentScreen.isActive())
            uiStack.pushScreen(currentScreen);
        currentScreen.setUp();
    }

    public void popScreen() {
        currentScreen.popScreen(uiStack);
    }

    public void homeScreen() {
        uiStack.clear();
        dispatcher.homeScreen();
    }

    public void createNewChildRecord() {
        dispatcher.newChild();
    }
}
