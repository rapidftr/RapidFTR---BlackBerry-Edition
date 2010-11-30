package com.rapidftr.process;

import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;

public class SyncAllProcess implements Process {

	ChildSyncService childSyncService;
    private FormService formService;
    String name;
	boolean canceled;

	public SyncAllProcess(ChildSyncService childSyncService, FormService formService) {
		super();
		this.childSyncService = childSyncService;
        this.formService = formService;
        this.name="Synchronize";
	}

	public String name() {
		return name;
	}

	public void startProcess() {
        formService.downloadForms();
		childSyncService.syncAllChildRecords();
	}

	public void stopProcess() {
		canceled = true;
        formService.cancelRequest();
		childSyncService.cancelRequest();
	}

	public boolean isCanceled() {
		return canceled;
	}

    public boolean isNotBackGround() {
        return true;
    }


}
