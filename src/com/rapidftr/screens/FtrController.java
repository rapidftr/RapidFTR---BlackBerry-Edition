package com.rapidftr.screens;

import net.rim.device.api.ui.UiEngine;

public class FtrController {

	private UiEngine engine;
	
	public FtrController(UiEngine engine) {
		this.engine = engine;
	}
	
	public void gotoHomeScreen() {
		engine.pushScreen(new HomeScreen());
	}
	
}
