package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.UploadChildrenRecordsScreen;
import com.rapidftr.services.RequestCallBackImpl;
import com.rapidftr.services.UploadChildrenRecordsService;
import com.sun.me.web.request.Response;

public class UploadChildrenRecordsController extends Controller implements
		ControllerCallback {

	
	private UploadChildrenRecordsService childRecordsUploadService;
	HttpRequestHandler listener;
	public UploadChildrenRecordsController(CustomScreen screen, UiStack uiStack,
			UploadChildrenRecordsService childRecordsUploadService) {
		super(screen, uiStack);
		this.childRecordsUploadService = childRecordsUploadService;
		RequestCallBackImpl callback = new RequestCallBackImpl((ScreenCallBack) screen,this);
		listener = new HttpRequestHandler(callback);
		childRecordsUploadService.setListener(listener);
		screen.setController(this);
	}

	public void uploadChildRecords() {
		listener.setRequestInProgress();
		childRecordsUploadService.uploadChildRecords();
		((UploadChildrenRecordsScreen) screen).resetProgressBar();
		show();
	}


	public void cancelUpload() {
		listener.cancelRequestInProgress();
		childRecordsUploadService.cancelUploadOfChildRecords();
	}

	public void login() {
		dispatcher.login();
	}

	public void uploadChildRecord(Child child) {
		listener.setRequestInProgress();
		childRecordsUploadService.uploadChildRecord(child);
		((UploadChildrenRecordsScreen) screen).resetProgressBar();
		show();
	}

	public void onRequestFailure(Exception exception) {
		((UploadChildrenRecordsScreen) screen).uploadFailed();
		
	}

	public void onRequestSuccess(Object context, Response result) {
		((UploadChildrenRecordsScreen) screen).uploadCompleted();
		
	}

}
