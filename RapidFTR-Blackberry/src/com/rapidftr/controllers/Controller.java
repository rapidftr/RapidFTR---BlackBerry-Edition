package com.rapidftr.controllers;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;

abstract public class Controller {

	protected Dispatcher dispatcher;
	protected CustomScreen currentScreen;
	protected UiStack uiStack;

	public Controller(CustomScreen screen, UiStack uiStack) {
		this.currentScreen = screen;
		this.uiStack = uiStack;
		this.currentScreen.setController(this);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void show() {

		if (!currentScreen.isActive())
			uiStack.pushScreen(currentScreen);
		currentScreen.setUp();
	}

	public void popScreen() {
		currentScreen.popScreen(uiStack);
	}

}
