package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.services.ChildService;


public class ViewChildrenController {
    private final ViewChildrenScreen screen;
    private final UiStack uiStack;
    private final ChildService childService;
    private ApplicationRootController applicationRootController;

    public ViewChildrenController(ViewChildrenScreen screen, UiStack uiStack, ChildService childService) {

        this.screen = screen;
        this.uiStack = uiStack;
        this.childService = childService;
        screen.setViewChildrenController(this);
    }

    public void show() {
        Child[] children = childService.getAllChildren();
        screen.setChildren(children);
        uiStack.pushScreen(screen);
    }

    public void showChild(Child child) {
        this.applicationRootController.viewChild(child);
        
    }

    public void setApplicationRootController(ApplicationRootController applicationRootController) {
        this.applicationRootController = applicationRootController;
    }
}
