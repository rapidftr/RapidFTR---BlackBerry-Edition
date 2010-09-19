package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.RequestAwareController;
import com.rapidftr.model.Child;
import com.rapidftr.screens.SyncChildScreen;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;

public class SyncController extends RequestAwareController {
	final ChildSyncService childSyncService;
	final FormService formSyncService;
	public SyncController(CustomScreen screen, UiStack uiStack,
			ChildSyncService childSyncService, FormService formSyncService) {
		super(screen, uiStack, childSyncService);
		this.childSyncService = childSyncService;
		this.formSyncService = formSyncService;
	}

	public void cancelProcess() {
		service.cancelRequest();
	}

	public void syncChildRecord(Child child) {
		((ChildSyncService) service).syncChildRecord(child);
		((SyncChildScreen) currentScreen).resetProgressBar();
		show();
	}

	public void syncAllChildRecords() {
		try {
			((ChildSyncService) service).syncAllChildRecords();
			//((SyncChildScreen) screen).resetProgressBar();
			show();
		} catch (Exception e) {
			requestHandler.markProcessFailed();
		}
	}

	public void clearOfflineData() {
		((ChildSyncService) service).clearState();		
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


}
