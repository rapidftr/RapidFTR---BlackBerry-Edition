package com.rapidftr.process;

import com.rapidftr.services.FormService;

public class FormSyncProcess implements Process {

	FormService service;
	boolean cancel;
	public FormSyncProcess(FormService service) {
		super();
		this.service = service;
	}

	public String name() {
		return "Form Sync";
	}

	public void startProcess() {
		service.downloadForms();
	}

	public void stopProcess() {
		service.cancelRequest();
		cancel = true;
	}
	
	public boolean isCanceled() {
		return cancel;
	}

}
