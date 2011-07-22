package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.*;
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
    private final FlagRecordController flagRecordController;

    public Dispatcher(ControllerFactory controllerFactory) {
        this.homeScreenController = controllerFactory.homeScreenControllerWith(this);
        this.manageChildController = controllerFactory.manageChildControllerWith(this);
        this.viewChildrenController = controllerFactory.viewChildrenControllerWith(this);
        this.childPhotoController = controllerFactory.viewChildPhotoControllerWith(this);
        this.childHistoryController = controllerFactory.childHistoryControllerWith(this);
        this.searchChildController = controllerFactory.searchChildControllerWith(this);
        this.loginController = controllerFactory.loginControllerWith(this);
        this.childController = controllerFactory.viewChildControllerWith(this);
        this.syncController = controllerFactory.syncControllerWith(this);
        this.resetDeviceController = controllerFactory.resetDeviceController();
        this.contactScreenController = controllerFactory.contactScreenControllerWith(this);
        this.flagRecordController = controllerFactory.flagRecordControllerWith(this);
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
        searchChildController.show();
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

	public void editChild(Child child, String selectedTab) {
		manageChildController.editChild(child, selectedTab);
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

	public void flagRecord(Child child) {
		flagRecordController.flagRecord(child);
	}

}
