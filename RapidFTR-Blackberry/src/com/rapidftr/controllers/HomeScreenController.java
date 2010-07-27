package com.rapidftr.controllers;

import com.rapidftr.screens.Screen;
import com.rapidftr.screens.UiStack;

public class HomeScreenController extends Controller {

	public HomeScreenController(Screen screen, UiStack uiStack) {
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
