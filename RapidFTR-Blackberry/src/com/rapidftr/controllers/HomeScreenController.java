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
		dispatcher.viewChildren();
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();

	}

	public void newChild() {
		dispatcher.newChild();
	}


	public void syncAll() {
		dispatcher.syncAll();
	}

	public void showSearch() {
		dispatcher.searchChild();
	}

	public void cleanAll() {
		dispatcher.cleanAll();		
	}

}
