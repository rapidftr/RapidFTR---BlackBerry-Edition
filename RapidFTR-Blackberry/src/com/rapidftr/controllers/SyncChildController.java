package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.SyncChildScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.ChildSyncService;

public class SyncChildController extends RequestAwareController {
	public SyncChildController(CustomScreen screen, UiStack uiStack,
			ChildSyncService childSyncService) {
		super(screen, uiStack, childSyncService);
	}

	public void cancelUpload() {
		service.cancelRequest();
	}

	public void login() {
		dispatcher.login();
	}

	public void syncChildRecord(Child child) {
		((ChildSyncService) service).uploadChildRecord(child);
		((SyncChildScreen) screen).resetProgressBar();
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

}
