package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controllers.HomeScreenController;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controllers.SearchChildController;
import com.rapidftr.controllers.SyncChildController;
import com.rapidftr.controllers.SynchronizeFormsController;
import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;

public class Dispatcher {

	private final HomeScreenController homeScreenController;
	private final LoginController loginController;
	private final ViewChildrenController viewChildrenController;
	private final SynchronizeFormsController synchronizeFormsController;
	private final ChildController newChildController;
	private final SyncChildController syncChildController;
	private final SearchChildController searchChildController;

	public Dispatcher(HomeScreenController homeScreenController,
			LoginController loginController,
			ViewChildrenController viewChildrenController,
			SynchronizeFormsController synchronizeFormsController,
			ChildController newChildController,
			SyncChildController uploadChildRecordsController,
			SearchChildController searchChildController) {

		this.homeScreenController = homeScreenController;
		this.loginController = loginController;
		this.viewChildrenController = viewChildrenController;
		this.synchronizeFormsController = synchronizeFormsController;
		this.newChildController = newChildController;
		this.syncChildController = uploadChildRecordsController;
		this.searchChildController = searchChildController;
		homeScreenController.setDispatcher(this);
		loginController.setDispatcher(this);
		viewChildrenController.setDispatcher(this);
		synchronizeFormsController.setDispatcher(this);
		newChildController.setDispatcher(this);
		uploadChildRecordsController.setDispatcher(this);
		searchChildController.setDispatcher(this);

	}

	public void homeScreen() {
		homeScreenController.show();
	}

	public void viewChildren() {
		viewChildrenController.show();
	}

	public void viewChild(Child child) {
		newChildController.viewChild(child);
	}

	public void editChild(Child child) {
		newChildController.editChild(child);
	}

	public void login() {
		loginController.show();
	}

	public void synchronizeForms() {
		synchronizeFormsController.synchronizeForms();
	}

	public void newChild() {
		newChildController.newChild();
	}


	public void syncAll() {
		syncChildController.syncAllChildRecords();
	}

	public void viewChildHistory(Child child) {
		newChildController.showHistory(child);
	}

	public void searchChild() {
		searchChildController.show();
	}

	public void searchAndDisplayChildren(SearchChildFilter searchChildFilter) {
		viewChildrenController.searchAndDispalyChildren(searchChildFilter);
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
