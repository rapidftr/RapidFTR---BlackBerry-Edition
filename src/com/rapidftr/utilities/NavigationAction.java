package com.rapidftr.utilities;

public class NavigationAction {
	private int id;
	private boolean push;
	private int screenId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isPush() {
		return push;
	}

	public void setPush(boolean type) {
		this.push = type;
	}

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}
}
