package com.rapidftr.screens;

import net.rim.device.api.ui.UiEngine;

public class NavigationController {

	private UiEngine engine;
	
	public NavigationController(UiEngine engine) {
		this.engine = engine;
	}
	
	public void gotoHomeScreen() {
		engine.pushScreen(new HomeScreen());
	}
	
}
