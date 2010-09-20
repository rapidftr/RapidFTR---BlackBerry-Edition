package com.rapidftr.process;

import com.rapidftr.model.Child;
import com.rapidftr.services.ChildSyncService;

public class ChildSyncProcess implements Process {

	ChildSyncService service;
    Child child;
	public ChildSyncProcess(ChildSyncService service) {
		super();
		this.service = service;
	}

	public String name() {
		return "Child Sync";
	}

	public void startProcess() {
		if(child!=null){
		 service.syncChildRecord(child);
		}
	}

	public void stopProcess() {
		service.cancelRequest();
	}

	public void setChild(Child child) {
		this.child = child;
	}
	

}
