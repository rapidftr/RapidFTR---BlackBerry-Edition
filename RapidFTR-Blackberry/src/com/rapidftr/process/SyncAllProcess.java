package com.rapidftr.process;

import com.rapidftr.services.ChildSyncService;
import com.rapidftr.services.FormService;

public class SyncAllProcess implements Process {

	ChildSyncService childSyncService;
    private FormService formService;
	boolean canceled;

	public SyncAllProcess(ChildSyncService childSyncService, FormService formService) {
		super();
		this.childSyncService = childSyncService;
        this.formService = formService;
	}

	public String name() {
		return "Synchronize";
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
