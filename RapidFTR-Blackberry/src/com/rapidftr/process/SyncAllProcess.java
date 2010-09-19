package com.rapidftr.process;

import com.rapidftr.services.ChildSyncService;

public class SyncAllProcess implements Process {

	ChildSyncService service;

	public SyncAllProcess(ChildSyncService service) {
		super();
		this.service = service;
	}

	public String name() {
		return "Sync All";
	}

	public void startProcess() {
		service.syncAllChildRecords();
	}

	public void stopProcess() {
		service.cancelRequest();
	}

}
