package com.rapidftr.controllers;

import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;
import com.rapidftr.services.LoginService;

public class ResetDeviceController {
	FormService formService;
	ChildSyncService childSyncService;
	LoginService loginService;

	public ResetDeviceController(FormService formService,
			ChildSyncService childSyncService, LoginService loginService) {
		super();
		this.formService = formService;
		this.childSyncService = childSyncService;
		this.loginService = loginService;
	}

	public void resetDevice() {
		formService.clearState();
		loginService.clearLoginState();
		childSyncService.clearState();
	}

}
