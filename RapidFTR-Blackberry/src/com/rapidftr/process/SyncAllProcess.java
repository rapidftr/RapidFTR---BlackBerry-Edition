package com.rapidftr.process;

import com.rapidftr.services.ChildSyncService;

public class SyncAllProcess implements Process {

	ChildSyncService service;
	String name;

	public SyncAllProcess(ChildSyncService service) {
		super();
		this.service = service;
		this.name="Sync All";
	}

	public String name() {
		return name;
	}

	public void startProcess() {
		service.syncAllChildRecords();
	}

	public void stopProcess() {
		service.cancelRequest();
	}
	
	

}
