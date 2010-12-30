package com.rapidftr.process;

import com.rapidftr.model.Child;
import com.rapidftr.services.ChildSyncService;

public class ChildSyncProcess implements Process {

	ChildSyncService service;
    Child child;
    boolean cancel;
	public ChildSyncProcess(ChildSyncService service) {
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
		cancel = true;
		service.cancelRequest();
	}

	public void setChild(Child child) {
		this.child = child;
	}
	
	public boolean isCanceled() {
		return cancel;
	}

    public boolean isNotBackGround() {
        return true;  
    }


}
