package com.rapidftr.controllers.internal;

import com.rapidftr.controllers.*;
import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.process.Process;
import com.rapidftr.screens.HomeScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.DateFormatter;
import com.rapidftr.utilities.Settings;

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

    public Dispatcher(LoginController loginController, ViewChildController childController,
                      SyncController syncController,
                      ResetDeviceController restController,
                      ContactInformationController contactScreenController,
                      ViewChildrenController viewChildrenController,
                      ViewChildPhotoController childPhotoController,
                      ChildHistoryController showHistoryController,
                      SearchChildController searchChildController,
                      Settings settings,
                      UiStack uiStack,
                      DateFormatter dateFormatter,
                      FormStore formStore,
                      ChildrenRecordStore childrenRecordStore) {

        HomeScreen homeSreen = new HomeScreen(settings);
        this.homeScreenController = new HomeController(homeSreen, uiStack, settings, this);

        ManageChildScreen manageChildScreen = new ManageChildScreen(settings, dateFormatter);
        this.manageChildController = new ManageChildController(manageChildScreen, uiStack, formStore, childrenRecordStore, this);

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
