package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.ViewChildScreen;

public class ViewChildController extends Controller {

	public ViewChildController(ViewChildScreen viewChildScreen, UiStack uiStack) {
		super(viewChildScreen, uiStack);
	}

	public void show(Child child) {
		((ViewChildScreen) screen).setChild(child);
		super.show();
	}

}
