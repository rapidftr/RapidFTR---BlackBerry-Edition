package com.rapidftr.screens;

import com.rapidftr.NavigationController;

import net.rim.device.api.ui.container.MainScreen;

abstract public class DisplayPage extends MainScreen implements Page {
	public static final int RETURN_HOME_ACTION = 100;
	public static final int POP_ACTION = 90;
	
	public DisplayPage() {
		this(0);
	}
	
	public DisplayPage(long style) {
		super(style);
	}
	
	protected boolean navigationClick(int status, int time) {
		return true;
	}

	protected void pushScreen(int actionId, Object userInfo) {
		NavigationController controller = NavigationController.getInstance(this.getUiEngine());
		
		controller.pushScreen(this, actionId, userInfo);
	}
	
	protected void popScreen(int actionId, Object userInfo) {
		NavigationController.getInstance(this.getUiEngine()).popScreen(actionId, userInfo);
	}
	
	public void initializePage(Object userInfo) {
		
		
	}

	public void updatePage(Object userInfo, DisplayPage source) {
		
		
	}

}
