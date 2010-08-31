package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.ChildChangeLogScreen;
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
	
	public void showHistory(Child child)
	{
		ChildChangeLogScreen historyScreen = new ChildChangeLogScreen(child);
		uiStack.pushScreen(historyScreen);
		historyScreen.setUp();
	}

	public void editChild(Child child) {
		dispatcher.editChild(child);
	}

	public void viewChildHistory(Child child) {
		dispatcher.viewChildHistory(child);
	}
}
