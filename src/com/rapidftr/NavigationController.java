package com.rapidftr;

import java.util.Vector;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

import com.rapidftr.screens.DisplayPage;
import com.rapidftr.screens.Page;
import com.rapidftr.utilities.NavigationAction;
import com.rapidftr.utilities.NavigationConfig;
import com.rapidftr.utilities.NavigationInfo;

public class NavigationController {
	public static final int LOGIN_SCREEN = 0;
	public static final int HOME_SCREEN = 1;
	public static final int IDENTIFICATION_SCREEN = 2;
	public static final int NAVIGATION_SCREEN = 3;
	public static final int RECORD_CREATION_SCREEN = 4;
	public static final int RECORD_EDIT_SCREEN = 5;
	public static final int RECORD_REVIEW_SCREEN = 6;
	public static final int SEARCH_SCREEN = 7;
	public static final int ADD_FAMILY_SCREEN = 8;

	private static NavigationController instance;

	private UiEngine engine;
	private Vector navigationConfig;

	public static synchronized NavigationController getInstance(UiEngine engine) {
		if (instance == null) {
			instance = new NavigationController(engine);
		}

		return instance;
	}

	private NavigationController(UiEngine engine) {
		this.engine = engine;

		try {
			navigationConfig = NavigationConfig.getInstance()
					.getConfiguration();
		} catch (Exception e) {
			Dialog.alert("Failed to load config data");
		}
	}

	public void pushScreen(Screen screen, int actionId, Object userInfo) {
		pushScreen(getScreenId(screen), actionId, userInfo);
	}
	
	private void pushScreen(int screenId, int actionId, Object userInfo) {
		MainScreen screen = getPushScreen(screenId, actionId);

		((Page) screen).initializePage(userInfo);

		engine.pushScreen(screen);
	}

	public void popScreen(int actionId, Object userInfo) {
		Screen screen = engine.getActiveScreen();

		int popToScreenId = getPopToScreen(screen, actionId);

		DisplayPage parentPage;

		if (popToScreenId != -1) {
			Screen popToScreen = popToScreen(screen, popToScreenId);

			parentPage = (DisplayPage) (popToScreen);
		} else {
			engine.popScreen(screen);

			parentPage = (DisplayPage) (engine.getActiveScreen());
		}

		parentPage.updatePage(userInfo, (DisplayPage)screen);
	}

	private MainScreen getPushScreen(int screenId, int action) {
		MainScreen screen = null;

		try {
			Class clazz = getPushScreenClass(screenId, action);

			screen = (MainScreen) clazz.newInstance();
		} catch (Exception e) {

		}

		return screen;
	}

	private Class getPushScreenClass(int screenId, int actionId) {
		NavigationInfo screenInfo = getNavigationInfo(screenId);

		int pushScreenId = getScreenIdForAction(screenInfo, actionId);

		Class clazz = null;

		if (pushScreenId != -1) {
			String name = "com.rapidftr.screens." + getScreenName(pushScreenId);

			try {
				clazz = Class.forName(name);
			} catch (Exception e) {
			}
		}

		return clazz;
	}

	private int getPopToScreen(Screen screen, int actionId) {
		int screenId = getScreenId(screen);

		NavigationInfo screenInfo = getNavigationInfo(screenId);

		return getScreenIdForAction(screenInfo, actionId);
	}

	private int getScreenIdForAction(NavigationInfo screenInfo, int actionId) {
		int screenId = -1;
		NavigationAction actions[] = screenInfo.getActions();

		for (int j = 0; j < actions.length; j++) {
			if (actions[j].getId() == actionId) {
				screenId = actions[j].getScreenId();
				break;
			}
		}
		
		return screenId;
	}
	
	private String getScreenName(int screenId) {
		NavigationInfo screenInfo = getNavigationInfo(screenId);

		return screenInfo.getScreenName();
	}

	private int getScreenId(Screen screen) {
		int id = -1;
		String name = screen.getClass().getName();

		for (int i = 0; i < navigationConfig.size(); i++) {
			NavigationInfo info = (NavigationInfo) navigationConfig
					.elementAt(i);

			if (name.indexOf(info.getScreenName()) != -1) {
				id = info.getScreenId();
				break;
			}
		}

		return id;
	}

	public Screen popToScreen(Screen screen, int screenId) {
		String screenName = getScreenName(screenId);

		Screen nextScreen = screen;
		Screen parentScreen;

		while ((parentScreen = nextScreen.getScreenBelow()) != null) {
			if (nextScreen.getClass().getName().indexOf(screenName) == -1) {
				engine.popScreen(nextScreen);

				nextScreen = parentScreen;
			} else {
				break;
			}
		}

		return nextScreen;
	}

	private NavigationInfo getNavigationInfo(int screenId) {
		NavigationInfo navigationInfo = null;

		for (int i = 0; i < navigationConfig.size(); i++) {
			NavigationInfo screenInfo = (NavigationInfo) navigationConfig
					.elementAt(i);

			if (screenInfo.getScreenId() == screenId) {
				navigationInfo = screenInfo;
				break;
			}
		}

		return navigationInfo;
	}
}
