package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.net.ControllerCallback;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.ScreenCallBack;
import com.rapidftr.screens.CustomScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.screens.SyncAllScreen;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.RequestCallBackImpl;
import com.sun.me.web.request.Response;

public class SyncChildController extends Controller implements
		ControllerCallback {

	private ChildSyncService childSyncService;
	HttpRequestHandler listener;
	public SyncChildController(CustomScreen screen, UiStack uiStack,
			ChildSyncService childRecordsUploadService) {
		super(screen, uiStack);
		this.childSyncService = childRecordsUploadService;
		RequestCallBackImpl callback = new RequestCallBackImpl((ScreenCallBack) screen,this);
		listener = new HttpRequestHandler(callback);
		childRecordsUploadService.setListener(listener);
		screen.setController(this);
	}


	public void cancelUpload() {
		listener.cancelRequestInProgress();
		childSyncService.cancelUploadOfChildRecords();
	}

	public void login() {
		dispatcher.login();
	}

	public void uploadChildRecord(Child child) {
		listener.setRequestInProgress();
		childSyncService.uploadChildRecord(child);
		((SyncAllScreen) screen).resetProgressBar();
		show();
	}
	

	public void uploadChildRecords() {
		listener.setRequestInProgress();
		childSyncService.uploadChildRecords();
		((SyncAllScreen) screen).resetProgressBar();
		show();
	}

	
	public void syncAllChildRecords() {
		try{
		listener.setRequestInProgress();
		childSyncService.syncAllChildRecords();
		((SyncAllScreen) screen).resetProgressBar();
		show();
		}catch (Exception e) {
			onRequestFailure(e);
		}
	}


	public void onRequestFailure(Exception exception) {
		((ScreenCallBack) screen).onProcessFail();
	}

	public void onRequestSuccess(Object context, Response result) {
		((ScreenCallBack) screen).onProcessComplete();		
	}

}
