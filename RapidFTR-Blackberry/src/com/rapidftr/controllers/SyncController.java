package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpBatchRequestHandler;
import com.rapidftr.process.ChildSyncProcess;
import com.rapidftr.process.FormSyncProcess;
import com.rapidftr.process.Process;
import com.rapidftr.process.SyncAllProcess;
import com.rapidftr.screens.SyncScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.ControllerCallback;
import com.rapidftr.services.FormService;
import com.rapidftr.services.RequestAwareService;
import com.rapidftr.services.RequestCallBackImpl;
import com.rapidftr.services.ScreenCallBack;

public class SyncController extends Controller implements ControllerCallback {

	private Process currentRunningProcess;

    final ChildSyncProcess childSyncProcess;
	final Process syncAllProcess;
	final Process formSyncProcess;
	
	protected HttpBatchRequestHandler requestHandler;
	private ScreenCallBack screenCallBack;



	public SyncController(CustomScreen screen, UiStack uiStack,
			ChildSyncService childSyncService, FormService formSyncService) {
		super(screen, uiStack);
		screen.setController(this);
		screenCallBack = (ScreenCallBack) screen;
		setUpRequestHandlerForService(formSyncService);
		setUpRequestHandlerForService(childSyncService);
		childSyncProcess = new ChildSyncProcess(childSyncService);
		syncAllProcess = new SyncAllProcess(childSyncService, formSyncService);
		formSyncProcess = new FormSyncProcess(formSyncService);
	}

	private void setUpRequestHandlerForService(RequestAwareService service) {
		requestHandler = service.getRequestHandler();
		if (requestHandler != null) {
			((RequestCallBackImpl) requestHandler.getRequestCallBack())
					.setScreenCallback(screenCallBack);
			((RequestCallBackImpl) requestHandler.getRequestCallBack())
					.setControllerCallback(this);
		}
	}

	public void syncChildRecord(Child child) {
		childSyncProcess.setChild(child);
		setAndStartCurrentProcess(childSyncProcess);
	}

	public void synchronize() {
		setAndStartCurrentProcess(syncAllProcess);
	}

	public void synchronizeForms() {
		setAndStartCurrentProcess(formSyncProcess);
	}

    private void setAndStartCurrentProcess(Process process) {
        if (this.currentRunningProcess == null) {
            this.currentRunningProcess = process;
            ((SyncScreen) currentScreen).attachProcess(this.currentRunningProcess);
            if (process.isNotBackGround()) {
                show();
            }
            this.currentRunningProcess.startProcess();
        } else if (this.currentRunningProcess == process) {
            if (this.currentRunningProcess.isCanceled()) {
                this.currentRunningProcess.startProcess();
            }
            if (process.isNotBackGround()) {
                show();
            }
        } else {
            ((SyncScreen) currentScreen).showRunninngProcessAlert();
        }

    }

	public void onProcessComplete(boolean status) {
		currentRunningProcess = null;
	}

    public void beforeProcessStart() {
		((SyncScreen) currentScreen).onProcessStart();
	}

	public void clearProcess() {
		currentRunningProcess.stopProcess();
		currentRunningProcess = null;
	}

}
