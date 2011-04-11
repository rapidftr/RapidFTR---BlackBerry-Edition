package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.ViewChildController;
import com.rapidftr.controllers.ChildHistoryController;
import com.rapidftr.controllers.ContactInformationController;
import com.rapidftr.controllers.HomeController;
import com.rapidftr.controllers.LoginController;
import com.rapidftr.controllers.ManageChildController;
import com.rapidftr.controllers.ResetDeviceController;
import com.rapidftr.controllers.SearchChildController;
import com.rapidftr.controllers.SyncController;
import com.rapidftr.controllers.ViewChildPhotoController;
import com.rapidftr.controllers.ViewChildrenController;
import com.rapidftr.datastore.Children;
import com.rapidftr.model.Child;
import com.rapidftr.process.Process;

public class Dispatcher {
	private final HomeController homeScreenController;
	private final LoginController loginController;
	private final ViewChildController childController;
	private final SyncController syncController;
	private ResetDeviceController resetDeviceController;
	private ContactInformationController contactScreenController;
	private final ManageChildController manageChildController;
	private final ViewChildrenController viewChildrenController;
	private final ViewChildPhotoController childPhotoController;
	private final ChildHistoryController childHistoryController;
	private final SearchChildController searchChildController;

	public Dispatcher(HomeController homeScreenController,
			LoginController loginController, ViewChildController childController,
			SyncController syncController,
			ResetDeviceController restController,
			ContactInformationController contactScreenController,
			ManageChildController manageChildController,
			ViewChildrenController viewChildrenController,
			ViewChildPhotoController childPhotoController,
			ChildHistoryController showHistoryController, SearchChildController searchChildController) {
		this.homeScreenController = homeScreenController;
		this.manageChildController = manageChildController;
		this.manageChildController.setDispatcher(this);
		this.viewChildrenController = viewChildrenController;
		this.viewChildrenController.setDispatcher(this);
		this.childPhotoController = childPhotoController;
		this.childPhotoController.setDispatcher(this);
		this.childHistoryController = showHistoryController;
		this.childHistoryController.setDispatcher(this);
		this.searchChildController = searchChildController;
		this.searchChildController.setDispatcher(this);
		this.homeScreenController.setDispatcher(this);
		this.loginController = loginController;
		this.loginController.setDispatcher(this);
		this.childController = childController;
		this.childController.setDispatcher(this);
		this.syncController = syncController;
		this.syncController.setDispatcher(this);
		this.resetDeviceController = restController;
		this.contactScreenController = contactScreenController;
		this.contactScreenController.setDispatcher(this);
	}

	public void homeScreen() {
		homeScreenController.show();
	}

	public void viewChildren() {
		viewChildrenController.viewAllChildren();
	}

	public void synchronizeForms() {
		syncController.synchronizeForms();
	}

	public void newChild() {
		manageChildController.newChild();
	}

	public void synchronize() {
		syncController.synchronize();
	}

	public void searchChild() {
		searchChildController.showChildSearchScreen();
	}

	public void syncChild(Child child) {
		syncController.syncChildRecord(child);
	}

	public void resetDevice() {
		resetDeviceController.resetDevice();
	}

	public void login(Process process) {
		loginController.showLoginScreen(process);

	}

	public void showcontact() {
		contactScreenController.show();
	}

	public void editChild(Child child) {
		manageChildController.editChild(child);

	}

	public void viewChild(Child child) {
		childController.viewChild(child);
		
	}

	public void viewChildren(Children children) {
		viewChildrenController.viewChildren(children);
	
	}

	public void viewChildPhoto(Child child) {
		childPhotoController.viewChildPhoto(child);
		
	}

	public void showHistory(Child child) {
		childHistoryController.showHistory(child);
	}

}
