package com.rapidftr.screens;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

public class UiStack {

	private final UiApplication application;

	public UiStack(UiApplication application) {
		this.application = application;
		// TODO Auto-generated constructor stub
	}

	public void pushScreen(Screen screen) {
		application.pushScreen(screen);
		
	}

}
