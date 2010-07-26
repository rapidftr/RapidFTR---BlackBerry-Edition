package com.rapidftr.screens;

import com.rapidftr.controllers.Controller;
import com.rapidftr.controls.TitleField;
import com.rapidftr.utilities.Styles;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.Background;
import net.rim.device.api.ui.decor.BackgroundFactory;

public abstract class Screen extends MainScreen {

	Controller controller;
	private TitleField titleField;

	public Screen() {

		titleField = new TitleField();
		setBackground(BackgroundFactory
				.createSolidBackground(Styles.COLOR_SCREEN_BACKGROUND));
		add(titleField);
	}

	public void setController(Controller controller) {
		this.controller = controller;

	}

	public boolean isActive() {

		MainScreen activeScreen = (MainScreen) UiApplication.getUiApplication()
				.getActiveScreen();

		if (activeScreen == null)
			return false;

		return UiApplication.getUiApplication().getActiveScreen().equals(this);
	}

	public abstract void setUp();
	public abstract void cleanUp();
	

	public void popScreen(final UiStack uiStack) {
		
		final Screen screen = this;
		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				uiStack.popScreen(screen);
			}
		});
		
	}


}
