package com.rapidftr.controllers;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;

public class HomeScreenController extends Controller {

	public HomeScreenController(CustomScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}

	public void login() {
		dispatcher.login();
	}

	public void viewChildren() {
		dispatcher.viewChildern();
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();
		
	}

	public void newChild() {
		dispatcher.newChild();
	}


}
