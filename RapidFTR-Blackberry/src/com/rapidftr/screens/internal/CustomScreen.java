package com.rapidftr.screens.internal;

import net.rim.device.api.system.Characters;
import net.rim.device.api.system.KeyListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controls.TitleField;
import com.rapidftr.utilities.Styles;

public abstract class CustomScreen extends MainScreen implements KeyListener {

	protected Controller controller;
	private TitleField titleField;
	protected static final XYEdges PADDING = new XYEdges(4, 4, 4, 4);

	public CustomScreen() {
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

	public void setUp() {

	};

	public void popScreen(final UiStack uiStack) {
		final CustomScreen screen = this;
		UiApplication.getUiApplication().invokeLater(new Runnable() {

			public void run() {
				if(screen.isActive()){
					uiStack.popScreen(screen);					
				}
			}
		});

	}

	protected void clearFields() {
		int fieldCount = this.getFieldCount();
		if (fieldCount > 0)
			this.deleteRange(0, fieldCount);
		add(new TitleField());
	}

	protected void makeMenu(Menu menu, int instance) {
		menu.add(new MenuItem("Main Menu", 0, 1) {
			public void run() {
				controller.homeScreen();
			}
		});
		super.makeMenu(menu, instance);
	}

	public boolean keyChar(char key, int status, int time) {
		if (key == Characters.ESCAPE) {
			if (controller != null)
				controller.popScreen();
				return true;
		}

		return super.keyChar(key, status, time);
	}

	public boolean keyDown(int keycode, int time) {
		if (keycode == Characters.ESCAPE) {
			if (controller != null)
				controller.popScreen();
			return true;
		}
		return super.keyDown(keycode, time);
	}

	public boolean keyRepeat(int keycode, int time) {
		return super.keyRepeat(keycode, time);
	}

	public boolean keyStatus(int keycode, int time) {
		return super.keyStatus(keycode, time);
	}

	public boolean keyUp(int keycode, int time) {
		return super.keyUp(keycode, time);
	}

}
