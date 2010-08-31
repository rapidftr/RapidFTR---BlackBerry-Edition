package com.rapidftr.controllers;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;

abstract public class Controller {

	protected Dispatcher dispatcher;
	protected CustomScreen screen;
	protected UiStack uiStack;

	public Controller(CustomScreen screen, UiStack uiStack) {
		this.screen = screen;
		this.uiStack = uiStack;
		this.screen.setController(this);
	}

	public void setDispatcher(Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public void show() {

		if (!screen.isActive())
			uiStack.pushScreen(screen);
		screen.setUp();
	}

	public void popScreen() {
		screen.popScreen(uiStack);
	}

}
