package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.model.Child;
import com.rapidftr.net.ControllerCallback;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.ScreenCallBack;
import com.rapidftr.process.ChildSyncProcess;
import com.rapidftr.process.FormSyncProcess;
import com.rapidftr.process.Process;
import com.rapidftr.process.SyncAllProcess;
import com.rapidftr.screens.SyncScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.RequestAwareService;
import com.rapidftr.services.RequestCallBackImpl;

public class SyncController extends Controller implements ControllerCallback {

	private Process currentProcess;
	private Process previousProcess;

	final ChildSyncProcess childSyncProcess;
	final SyncAllProcess syncAllProcess;
	final FormSyncProcess formSyncProcess;

	protected HttpRequestHandler requestHandler;
	private ScreenCallBack screenCallBack;

	public SyncController(CustomScreen screen, UiStack uiStack,
			ChildSyncService childSyncService, FormService formSyncService) {
		// super(screen, uiStack, childSyncService);
		super(screen, uiStack);
		screen.setController(this);
		screenCallBack = (ScreenCallBack) screen;
		setUpRequestHandlerForService(formSyncService);
		setUpRequestHandlerForService(childSyncService);
		childSyncProcess = new ChildSyncProcess(childSyncService);
		syncAllProcess = new SyncAllProcess(childSyncService);
		formSyncProcess = new FormSyncProcess(formSyncService);
	}

	private void setUpRequestHandlerForService(RequestAwareService service) {
		requestHandler = service.getRequestHandler();
		if (requestHandler != null) {
			((RequestCallBackImpl) requestHandler.getRequestCallBack())
					.setScreenCallback(screenCallBack);
			((RequestCallBackImpl) requestHandler.getRequestCallBack())
					.setControllerCallback((ControllerCallback) this);
		}
	}

	public void syncChildRecord(Child child) {
		childSyncProcess.setChild(child);
		setAndStartCurrentProcess(childSyncProcess);
	}

	public void syncAllChildRecords() {
		setAndStartCurrentProcess(syncAllProcess);
	}

	public void synchronizeForms() {
		setAndStartCurrentProcess(formSyncProcess);
	}

	private void setAndStartCurrentProcess(Process process) {
		if (currentProcess == null) {
			currentProcess = process;
			((SyncScreen) currentScreen).setProcess(currentProcess);
			show();
			currentProcess.startProcess();
		} else if (currentProcess == process) {
			show();
		} else {
			((SyncScreen) currentScreen).showRunninngProcessAlert();
		}

	}

	public void afterProcessComplete() {
		previousProcess = currentProcess;
		currentProcess = null;
	}

	public void login() {
		dispatcher.login(previousProcess);
		requestHandler.cancelRequestInProgress();
	}

	public void beforeProcessStart() {
		// TODO Auto-generated method stub

	}

}
