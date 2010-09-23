package com.rapidftr.services;

public interface ControllerCallback {
	void onProcessComplete(boolean status);

	void beforeProcessStart();
}
