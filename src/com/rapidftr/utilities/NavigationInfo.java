package com.rapidftr.utilities;

import net.rim.device.api.util.Arrays;

public class NavigationInfo {

	private String screenName;
	private int screenId;
	private NavigationAction actions[];

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public void addAction(NavigationAction action) {
		actions = (actions == null) ? new NavigationAction[0] : actions;
		Arrays.add(actions, action);
	}

	public NavigationAction[] getActions() {
		return actions;
	}

	public void setActions(NavigationAction[] actions) {
		this.actions = actions;
	}
}

