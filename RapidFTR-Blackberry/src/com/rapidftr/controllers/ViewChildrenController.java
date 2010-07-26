package com.rapidftr.controllers;

import java.io.IOException;

import net.rim.device.api.ui.component.Dialog;

import com.rapidftr.model.Child;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.services.ChildService;

public class ViewChildrenController extends Controller {

	private final ChildService childService;

	public ViewChildrenController(ViewChildrenScreen screen, UiStack uiStack,
			ChildService childService) {
		super(screen, uiStack);
		this.childService = childService;
	}

	public void show() {
		Child[] children;
		try {
			children = childService.getAllChildren();
			((ViewChildrenScreen) screen).setChildren(children);
			uiStack.pushScreen(screen);
		} catch (IOException e) {
			//Alert user with proper error message
		}

	}

	public void showChild(Child child) {
		dispatcher.viewChild(child);
	}

}
