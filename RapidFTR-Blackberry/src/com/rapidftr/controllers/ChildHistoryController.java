package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ChildHistoryScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

public class ChildHistoryController extends Controller {


	public ChildHistoryController(CustomScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}

	public void showHistory(Child child) {
		getChildHistoryScreen().setChild(child);
		show();
	}

	private ChildHistoryScreen getChildHistoryScreen(){
		return (ChildHistoryScreen)currentScreen;
	}
}
