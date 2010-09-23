package com.rapidftr.process;

import com.rapidftr.services.ChildSyncService;

public class SyncAllProcess implements Process {

	ChildSyncService service;
	String name;
	boolean cancel;

	public SyncAllProcess(ChildSyncService service) {
		super();
		this.service = service;
		this.name="Sync Child Records";
	}

	public String name() {
		return name;
	}

	public void startProcess() {
		service.syncAllChildRecords();
	}

	public void stopProcess() {
		cancel = true;
		service.cancelRequest();
	}

	public boolean isCanceled() {
		return cancel;
	}
	
	

}
