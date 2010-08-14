package com.rapidftr.controllers;

import com.rapidftr.model.Child;

public class Dispatcher {

	private HomeScreenController homeScreenController;
	private LoginController loginController;
	private ViewChildrenController viewChildrenController;
	private ViewChildController viewChildController;
	private SynchronizeFormsController synchronizeFormsController;
	private NewChildController newChildController;
	private UploadChildrenRecordsController uploadChildRecordsController;

	public Dispatcher(HomeScreenController homeScreenController,
			LoginController loginController,
			ViewChildrenController viewChildrenController,
			ViewChildController viewChildController,
			SynchronizeFormsController synchronizeFormsController,NewChildController newChildController,UploadChildrenRecordsController uploadChildRecordsController) {

		this.homeScreenController = homeScreenController;
		this.loginController = loginController;
		this.viewChildrenController = viewChildrenController;
		this.viewChildController = viewChildController;
		this.synchronizeFormsController = synchronizeFormsController;
		this.newChildController = newChildController;
		this.uploadChildRecordsController = uploadChildRecordsController;

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

}
