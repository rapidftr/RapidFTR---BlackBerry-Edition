package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controllers.SyncChildController;
import com.rapidftr.controllers.SynchronizeFormsController;
import com.rapidftr.model.Child;

public class Dispatcher {

	private final HomeScreenController homeScreenController;
	private final LoginController loginController;
	private final SynchronizeFormsController synchronizeFormsController;
	private final ChildController childController;
	private final SyncChildController syncChildController;

	public Dispatcher(HomeScreenController homeScreenController,
			LoginController loginController,
			SynchronizeFormsController synchronizeFormsController,
			ChildController newChildController,
			SyncChildController uploadChildRecordsController) {

		this.homeScreenController = homeScreenController;
		this.loginController = loginController;
		this.synchronizeFormsController = synchronizeFormsController;
		this.childController = newChildController;
		this.syncChildController = uploadChildRecordsController;
		homeScreenController.setDispatcher(this);
		loginController.setDispatcher(this);
		synchronizeFormsController.setDispatcher(this);
		newChildController.setDispatcher(this);
		uploadChildRecordsController.setDispatcher(this);

	}

	public void homeScreen() {
		homeScreenController.show();
	}

	public void viewChildren() {
		childController.viewChildren();
	}


	public void login() {
		loginController.show();
	}

	public void synchronizeForms() {
		synchronizeFormsController.synchronizeForms();
	}

	public void newChild() {
		childController.newChild();
	}


	public void syncAll() {
		syncChildController.syncAllChildRecords();
	}

	public void searchChild() {
		childController.showChildSearchScreen();
	}

	public void syncChild(Child child) {
		syncChildController.syncChildRecord(child);		
	}

	public void cleanAll() {
		syncChildController.clearOfflineData();
		loginController.clearLoginState();
		synchronizeFormsController.clearOfflineData();
	}

}
