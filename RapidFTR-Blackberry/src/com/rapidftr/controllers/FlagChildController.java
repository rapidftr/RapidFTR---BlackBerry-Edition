package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.model.Child;
import com.rapidftr.screens.FlagReasonScreen;
import com.rapidftr.screens.internal.UiStack;

public class FlagChildController extends Controller {
	
	FlagReasonScreen flagReasonScreen;
	public FlagChildController(FlagReasonScreen flagReasonScreen, UiStack uiStack,
			Dispatcher dispatcher) {
		super(flagReasonScreen, uiStack, dispatcher);
		this.flagReasonScreen = flagReasonScreen;
	}

	public void flagRecord(Child child) {
		flagReasonScreen.setChild(child);
		show();
	}

}
