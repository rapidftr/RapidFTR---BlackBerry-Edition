package com.rapidftr.controllers;

import java.io.IOException;
import java.util.Vector;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.ChildService;
import com.rapidftr.services.ChildStoreService;
import com.rapidftr.services.UploadChildrenRecordsService;

public class SyncAllController extends Controller {

	private final ChildStoreService childStoreService;
	private final UploadChildrenRecordsService childRecordsUploadService;
	private final ChildService childService;

	
	public SyncAllController(CustomScreen screen, UiStack uiStack,
			ChildStoreService childStoreService,
			UploadChildrenRecordsService childRecordsUploadService,
			ChildService childService) {
		super(screen, uiStack);
		this.childStoreService = childStoreService;
		this.childRecordsUploadService = childRecordsUploadService;
		this.childService = childService;
	}


	public void syncAll() {
		//childRecordsUploadService.uploadChildRecords();
		try {
			childStoreService.syncAllChildrenWithStore(new Vector());
			childStoreService.syncAllChildrenWithStore(childService.getAllChildren());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
