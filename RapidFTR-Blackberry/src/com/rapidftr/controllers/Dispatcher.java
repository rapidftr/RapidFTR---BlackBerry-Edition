package com.rapidftr.controllers;

import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;

public class Dispatcher {

	private final HomeScreenController homeScreenController;
	private final LoginController loginController;
	private final ViewChildrenController viewChildrenController;
	private final ViewChildController viewChildController;
	private final SynchronizeFormsController synchronizeFormsController;
	private final ManageChildController newChildController;
	private final SyncChildController syncChildController;
	private final SearchChildController searchChildController;

	public Dispatcher(HomeScreenController homeScreenController,
			LoginController loginController,
			ViewChildrenController viewChildrenController,
			ViewChildController viewChildController,
			SynchronizeFormsController synchronizeFormsController,
			ManageChildController newChildController,
			SyncChildController uploadChildRecordsController,
			SearchChildController searchChildController) {

		this.homeScreenController = homeScreenController;
		this.loginController = loginController;
		this.viewChildrenController = viewChildrenController;
		this.viewChildController = viewChildController;
		this.synchronizeFormsController = synchronizeFormsController;
		this.newChildController = newChildController;
		this.syncChildController = uploadChildRecordsController;
		this.searchChildController = searchChildController;
		homeScreenController.setDispatcher(this);
		loginController.setDispatcher(this);
		viewChildrenController.setDispatcher(this);
		viewChildController.setDispatcher(this);
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
		viewChildController.show(child);
	}

	public void editChild(Child child) {
		newChildController.showEditScreenForChild(child);
	}

	public void login() {
		loginController.show();
	}

	public void synchronizeForms() {
		synchronizeFormsController.synchronizeForms();
	}

	public void newChild() {
		newChildController.show();
	}

	public void uploadChildRecords() {
		syncChildController.uploadChildRecords();
	}

	public void syncAll() {
		syncChildController.syncAllChildRecords();
	}

	public void viewChildHistory(Child child) {
		viewChildController.showHistory(child);
	}

	public void searchChild() {
		searchChildController.show();
	}

	public void searchAndDisplayChildren(SearchChildFilter searchChildFilter) {
		viewChildrenController.searchAndDispalyChildren(searchChildFilter);
	}

	public void syncChild(Child child) {
		syncChildController.uploadChildRecord(child);		
	}

}
