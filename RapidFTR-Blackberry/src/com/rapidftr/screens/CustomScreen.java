package com.rapidftr.screens;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.rapidftr.controllers.Controller;
import com.rapidftr.controls.TitleField;
import com.rapidftr.utilities.Styles;

public abstract class CustomScreen extends MainScreen {

	Controller controller;
	private TitleField titleField;
	protected static final XYEdges PADDING = new XYEdges(4, 4, 4, 4);

	public CustomScreen() 
	{
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
		final CustomScreen screen = this;		
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			
			public void run() {
				uiStack.popScreen(screen);
			}
		});
		
	}
	
	public void addLogo() {
		Bitmap bitmap = Bitmap.getBitmapResource("res/logo.jpg");
		if (bitmap == null) {
			return;
		}

		BitmapField field = new BitmapField(bitmap, FIELD_HCENTER);
		field.setPadding(PADDING);
		add(field);
	}


}
