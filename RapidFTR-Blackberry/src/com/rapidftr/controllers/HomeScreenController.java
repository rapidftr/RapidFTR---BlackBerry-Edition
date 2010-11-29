package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

public class HomeScreenController extends Controller {

	public HomeScreenController(CustomScreen screen, UiStack uiStack) {
		super(screen, uiStack);
	}

	public void viewChildren() {
		dispatcher.viewChildren();
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();
	}

	public void newChild() {
		dispatcher.newChild();
	}

	public void synchronize() {
		dispatcher.synchronize();
	}

	public void showSearch() {
		dispatcher.searchChild();
	}

	public void cleanAll() {
		dispatcher.resetDevice();
	}

	public void logIn() {
		dispatcher.login(null);
	}

}
