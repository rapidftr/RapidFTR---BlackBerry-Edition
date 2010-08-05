package com.rapidftr.controllers;

import org.json.me.JSONException;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.UploadChildRecordsScreen;
import com.rapidftr.services.UploadChildRecordsSeriviceListener;
import com.rapidftr.services.UploadChildRecordsService;

public class UploadChildRecordsController extends Controller implements UploadChildRecordsSeriviceListener {

	private boolean uploadInProgress = false;
	private UploadChildRecordsService childRecordsUploadService;
	

	public UploadChildRecordsController(CustomScreen screen, UiStack uiStack,UploadChildRecordsService childRecordsUploadService){
		super(screen, uiStack);
		this.childRecordsUploadService = childRecordsUploadService;
		childRecordsUploadService.setListener(this);
		screen.setController(this);
	}

	public void uploadChildRecords() {
		uploadInProgress = true;
		childRecordsUploadService.uploadChildRecords();
		((UploadChildRecordsScreen) screen).resetProgressBar();
		show();
	}

	public void onUploadComplete() {
		
		
	}

	public void onUploadFailed() {
		
		if (!uploadInProgress)
			return;
		
		uploadInProgress = false;
		((UploadChildRecordsScreen) screen).uploadFailed();
		
	}

	public void updateUploadStatus(int bytes, int total) {
		
		if (!uploadInProgress)
			return;

//		double size = ((double) bytes) / total;
//		((UploadChildRecordsScreen) screen)
//				.updateUploadProgessBar((int) (size * 100));
	 //System.out.println("Received :" + bytes);
	}

	public void onAuthenticationFailure() {
		if (!uploadInProgress)
			return;
		
		uploadInProgress = false;
		
//		try {
//			//formStore.storeForms(jsonFormsAsString);
//		} catch (JSONException e) {
//			
//			((SynchronizeFormsScreen) screen).downloadFailed();
//			return;
//		}
		
		((UploadChildRecordsScreen) screen).uploadCompleted();

	}

	public void onConnectionProblem() {
		if (!uploadInProgress)
			return;

		uploadInProgress = false;
		((UploadChildRecordsScreen) screen).uploadFailed();
		
	}

}
