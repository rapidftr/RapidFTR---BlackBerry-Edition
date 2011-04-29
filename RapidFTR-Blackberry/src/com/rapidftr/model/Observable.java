package com.rapidftr.model;

import net.rim.device.api.ui.FocusChangeListener;

public interface Observable {
	public void addFocusChangeObserver(FocusChangeListener observer);
}
