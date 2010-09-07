package com.rapidftr.controllers;

import org.json.me.JSONException;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.screens.SynchronizeFormsScreen;
import com.rapidftr.screens.UiStack;
import com.rapidftr.services.FormService;
import com.rapidftr.services.RequestCallBackImpl;
import com.sun.me.web.request.Response;

public class SynchronizeFormsController extends Controller implements
		ControllerCallback {

	FormStore formStore;
	FormService formService;
	HttpRequestHandler listener;

	public SynchronizeFormsController(FormService formService,
			FormStore formStore, UiStack uiStack,
			SynchronizeFormsScreen synchronizeFormsScreen) {
		super(synchronizeFormsScreen, uiStack);
		this.formStore = formStore;
		this.formService = formService;
		RequestCallBackImpl callback = new RequestCallBackImpl((ScreenCallBack) screen,this);
		listener = new HttpRequestHandler(callback);
		formService.setListener(listener);
	}

	public void synchronizeForms() {
		listener.setRequestInProgress();
		formService.downloadForms();
		((SynchronizeFormsScreen) screen).resetProgressBar();
		show();
	}

	public void stopSynchronizingForms() {
		listener.cancelRequestInProgress();
		formService.cancelDownloadOfForms();
	}

	public void login() {
		listener.cancelRequestInProgress();
		dispatcher.login();
	}

	public void onRequestFailure(Exception exception) {
		((SynchronizeFormsScreen) screen).downloadFailed();
	}

	public void onRequestSuccess(Object context, Response result) {
		try {
			formStore.storeForms(result.getResult().toString());
		} catch (JSONException e) {

			((SynchronizeFormsScreen) screen).downloadFailed();
			return;
		}
		((SynchronizeFormsScreen) screen).downloadCompleted();

	}

}
