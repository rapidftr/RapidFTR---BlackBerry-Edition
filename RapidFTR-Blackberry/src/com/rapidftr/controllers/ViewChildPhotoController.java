package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ChildPhotoScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

public class ViewChildPhotoController extends Controller {

	public ViewChildPhotoController(CustomScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}
	
	public void viewChildPhoto(Child child) {
		getChildPhotoScreen().setChild(child);
		show();
	}

	private ChildPhotoScreen getChildPhotoScreen(){
		return (ChildPhotoScreen)currentScreen;
	}
}
