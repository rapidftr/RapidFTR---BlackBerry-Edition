package com.rapidftr.controllers;

import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.UploadChildrenRecordsScreen;
import com.rapidftr.services.UploadChildrenRecordsSeriviceListener;
import com.rapidftr.services.UploadChildrenRecordsService;

public class UploadChildrenRecordsController extends Controller implements
		UploadChildrenRecordsSeriviceListener {

	private boolean uploadInProgress = false;
	private UploadChildrenRecordsService childRecordsUploadService;

	public UploadChildrenRecordsController(CustomScreen screen, UiStack uiStack,
			UploadChildrenRecordsService childRecordsUploadService) {
		super(screen, uiStack);
		this.childRecordsUploadService = childRecordsUploadService;
		childRecordsUploadService.setListener(this);
		screen.setController(this);
	}

	public void uploadChildRecords() {
		uploadInProgress = true;
		childRecordsUploadService.uploadChildRecords();
		((UploadChildrenRecordsScreen) screen).resetProgressBar();
		show();
	}

	public void onUploadComplete() {

		((UploadChildrenRecordsScreen) screen).uploadCompleted();
	}

	public void onUploadFailed() {

		if (!uploadInProgress)
			return;

		uploadInProgress = false;
		((UploadChildrenRecordsScreen) screen).uploadFailed();

	}

	public void updateUploadStatus(int bytes, int total) {

		if (!uploadInProgress)
			return;

		double size = ((double) bytes) / total;
		((UploadChildrenRecordsScreen) screen)
				.updateUploadProgessBar((int) (size * 100));

	}

	public void onAuthenticationFailure() {
		if (!uploadInProgress)
			return;

		uploadInProgress = false;

		((UploadChildrenRecordsScreen) screen).authenticationFailure();

	}

	public void onConnectionProblem() {
		if (!uploadInProgress)
			return;

		uploadInProgress = false;
		((UploadChildrenRecordsScreen) screen).connectionProblem();

	}

	public void cancelUpload() {
		uploadInProgress = false;
		childRecordsUploadService.cancelUploadOfChildRecords();
	}

	public void login() {
		dispatcher.login();
	}

}
