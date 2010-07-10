package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildScreen;

public class ViewChildController {
    private final ViewChildScreen viewChildScreen;
    private final UiStack uiStack;

    public ViewChildController(ViewChildScreen viewChildScreen, UiStack uiStack) {
        this.viewChildScreen = viewChildScreen;
        this.uiStack = uiStack;
    }


    public void show(Child child) {
        viewChildScreen.setChild(child);
        uiStack.pushScreen(viewChildScreen);
    }


}
