package com.rapidftr.controllers;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.ChildStoreService;

public class SyncAllController extends Controller {

	ChildStoreService childStoreService;

	public SyncAllController(CustomScreen screen, UiStack uiStack,
			ChildStoreService childStoreService) {
		super(screen, uiStack);
		this.childStoreService = childStoreService;
	}

	public void syncAll() {
		// TODO it will do 3 functions
		// •Send new local records to the database #148
		// •Send updated records to the database #173
		// •Get records from the database #174
		// childStoreService.syncAllChildrenRecordsWithServer();
	}

}
