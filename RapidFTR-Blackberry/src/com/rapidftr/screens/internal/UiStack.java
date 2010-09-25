package com.rapidftr.screens.internal;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.container.MainScreen;

public class UiStack {

	private final UiApplication application;

	public UiStack(UiApplication application) {
		this.application = application;
		// TODO Auto-generated constructor stub
	}

	public void pushScreen(Screen screen) {
		application.pushScreen(screen);
	}

    public void popScreen(MainScreen screen) {
    	if(application.getActiveScreen()==screen && application.getScreenCount()>0){         
    			application.popScreen(screen);
    		}
    }

	public void clear() {
		for (int i = application.getScreenCount();i>0 ; i--)
		{
			application.popScreen(application.getActiveScreen());
		}
	}

    public CustomScreen getCurrentScreen() {
        return (CustomScreen)application.getActiveScreen();
    }
}
