package com.rapidftr;

import com.rapidftr.screens.LoginScreen;

import net.rim.device.api.ui.UiApplication;

public class Main extends UiApplication {

	public static final String APPLICATION_NAME = "RapidFTR";
	
	/**
	 * Entry point for application.
	 */
	public static void main(String[] args) {

		// Create a new instance of the application.
		Main application = new Main();

		// To make the application enter the event thread and start processing
		// messages,
		// we invoke the enterEventDispatcher() method.
		application.enterEventDispatcher();
	}

	/**
	 * <p>
	 * The default constructor. Creates all of the RIM UI components and pushes
	 * the application's root screen onto the UI stack.
	 */
	public Main() {
		pushScreen( new LoginScreen());
	}
}
