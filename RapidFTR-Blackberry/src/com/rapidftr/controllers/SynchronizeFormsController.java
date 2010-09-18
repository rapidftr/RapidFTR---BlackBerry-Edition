package com.rapidftr.controllers;

import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.FormService;

public class SynchronizeFormsController extends RequestAwareController {

	public SynchronizeFormsController(FormService formService,
			UiStack uiStack,
			SynchronizeFormsScreen synchronizeFormsScreen) {
		super(synchronizeFormsScreen, uiStack,formService);
	}

	public void synchronizeForms() {
		((FormService) service).downloadForms();
		((SynchronizeFormsScreen) currentScreen).resetProgressBar();
		show();
	}

	public void stopSynchronizingForms() {
		((FormService) service).cancelRequest();
	}

	public void login() {
		requestHandler.cancelRequestInProgress();
		dispatcher.login();
	}

	public void clearOfflineData() {
		((FormService) service).clearState();
	}


}
