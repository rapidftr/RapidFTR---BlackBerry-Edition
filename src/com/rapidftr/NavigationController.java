package com.rapidftr;

import java.util.Vector;

import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.container.MainScreen;

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

			System.out.println("Nav Info " + navigationConfig);
		} catch (Exception e) {
			Dialog.alert("Failed to load config data");
		}
	}

	public void pushScreen(int screenId, int action, Object userInfo) {
		MainScreen screen = getPushScreen(screenId, action);

		((Page)screen).setUserInfo(userInfo);
		
		engine.pushScreen(screen);
	}

	public void popScreen(int screenId, int status, Object userInfo) {

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

	private Class getPushScreenClass(int screenId, int action) {
		int nextScreenId = -1;

		for (int i = 0; i < navigationConfig.size(); i++) {
			NavigationInfo screenInfo = (NavigationInfo) navigationConfig
					.elementAt(i);

			if (screenInfo.getScreenId() == screenId) {
				NavigationAction actions[] = screenInfo.getActions();

				for (int j = 0; j < actions.length; j++) {
					NavigationAction nextAction = actions[j];

					if (nextAction.isPush()) {
						if (nextAction.getId() == action) {
							nextScreenId = nextAction.getScreenId();
							break;
						}
					}
				}

				break;
			}
		}

		Class clazz = null;

		if (nextScreenId != -1) {
			String name = "com.rapidftr.screens." + getScreenName(nextScreenId);

			try {
				clazz = Class.forName(name);
			} catch (Exception e) {
			}
		}

		return clazz;
	}

	private String getScreenName(int screenId) {
		String screenName = null;

		for (int i = 0; i < navigationConfig.size(); i++) {
			NavigationInfo screenInfo = (NavigationInfo) navigationConfig
					.elementAt(i);

			if (screenInfo.getScreenId() == screenId) {
				screenName = screenInfo.getScreenName();
				break;
			}
		}

		return screenName;
	}
}
