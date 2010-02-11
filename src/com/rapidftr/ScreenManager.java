package com.rapidftr;

public interface ScreenManager {
	public static final int STATUS_CLOSE = 100;
	public static final int STATUS_SAVE = 200;
	
	void closeScreen(int status, Object userInfo);
}
