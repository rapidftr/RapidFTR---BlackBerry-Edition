package com.rapidftr.controllers.internal;

import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;

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

	protected void changeScreen(CustomScreen screen) {	
		currentScreen = screen;
		currentScreen.setController(this);
        show();
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
