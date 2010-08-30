package com.rapidftr.controllers;

import com.rapidftr.model.Child;

public class Dispatcher {

	private HomeScreenController homeScreenController;
	private LoginController loginController;
	private ViewChildrenController viewChildrenController;
	private ViewChildController viewChildController;
	private SynchronizeFormsController synchronizeFormsController;
	private ChildCreateUpdateController newChildController;
	private UploadChildrenRecordsController uploadChildRecordsController;
	private SyncAllController syncAllController;

	public Dispatcher(HomeScreenController homeScreenController,
			LoginController loginController,
			ViewChildrenController viewChildrenController,
			ViewChildController viewChildController,
			SynchronizeFormsController synchronizeFormsController,
			ChildCreateUpdateController newChildController,
			UploadChildrenRecordsController uploadChildRecordsController,
			SyncAllController syncAllController) {

		this.homeScreenController = homeScreenController;
		this.loginController = loginController;
		this.viewChildrenController = viewChildrenController;
		this.viewChildController = viewChildController;
		this.synchronizeFormsController = synchronizeFormsController;
		this.newChildController = newChildController;
		this.uploadChildRecordsController = uploadChildRecordsController;
		this.syncAllController = syncAllController;
		homeScreenController.setDispatcher(this);
		loginController.setDispatcher(this);
		viewChildrenController.setDispatcher(this);
		viewChildController.setDispatcher(this);
		synchronizeFormsController.setDispatcher(this);
		newChildController.setDispatcher(this);
		uploadChildRecordsController.setDispatcher(this);

	}

	public void homeScreen() {
		homeScreenController.show();
	}

	public void viewChildern() {
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
		uploadChildRecordsController.uploadChildRecords();
	}

	public void syncAll() {
		syncAllController.syncAll();
	}

	public void viewChildHistory(Child child) {
		viewChildController.showHistory(child);
	}

}
