package com.rapidftr.controllers;

import java.util.Vector;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.FormService;
import com.rapidftr.services.FormServiceListener;

public class SynchronizeFormsController extends Controller implements
		FormServiceListener {

	public static final Object CALLER = "caller";
	FormStore formStore;
	FormService formService;
	private boolean downloadInProgress = false;

	public SynchronizeFormsController(FormService formService,
			FormStore formStore, UiStack uiStack,
			SynchronizeFormsScreen synchronizeFormsScreen) {
		super(synchronizeFormsScreen, uiStack);
		this.formStore = formStore;
		this.formService = formService;
		formService.setListener(this);
	}

	public void synchronizeForms() {
		downloadInProgress = true;
		formService.downloadForms();
		((SynchronizeFormsScreen) screen).resetProgressBar();
		show();
	}

	public void stopSynchronizingForms() {
		downloadInProgress = false;
		formService.cancelDownloadOfForms();
	}

	public void login() {
		downloadInProgress = false;
		dispatcher.login();
	}

	public void onAuthenticationFailure() {

		if (!downloadInProgress)
			return;

		downloadInProgress = false;
		((SynchronizeFormsScreen) screen).onNotLoggedIn();

	}

	public void onConnectionProblem() {
		if (!downloadInProgress)
			return;

		downloadInProgress = false;
		((SynchronizeFormsScreen) screen).downloadFailed();

	}

	public void onDownloadComplete(Vector forms) {
		if (!downloadInProgress)
			return;
		downloadInProgress = false;
		((SynchronizeFormsScreen) screen).downloadCompleted();

	}

	public void updateDownloadStatus(int received, int total) {
		if (!downloadInProgress)
			return;

		double size = ((double) received) / total;
		((SynchronizeFormsScreen) screen)
				.updateDownloadProgessBar((int) (size * 100));
	//	System.out.println("Received :" + received);

	}

	public void downloadFailed() {
		if (!downloadInProgress)
			return;

		downloadInProgress = false;
		((SynchronizeFormsScreen) screen).downloadFailed();

	}
}
