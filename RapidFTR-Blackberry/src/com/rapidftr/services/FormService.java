package com.rapidftr.services;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class FormService extends RequestAwareService{

	FormStore formStore;

	public FormService(HttpService httpService,FormStore formStore) {
		super(httpService);
		this.formStore = formStore;
	}

	public void downloadForms() {
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		requestHandler.get("published_form_sections", null, httpArgs, null);
	}


	public void onRequestSuccess(Object context, Response result) {
		try {
			formStore.storeForms(result.getResult().toString());
		} catch (Exception e) {

			requestHandler.markProcessFailed();
			return;
		}
		requestHandler.markProcessComplete();		
	}

	public void clearState() {
	formStore.clearState();
	}

}
