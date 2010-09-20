package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;
import com.rapidftr.screens.ChildHistoryScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildStoreService;
import com.rapidftr.utilities.ImageCaptureListener;

public class ChildController extends Controller {

	private final FormStore formStore;
	private final ChildStoreService childStoreService;
	private final ManageChildScreen manageChildScreen;
	private final ViewChildScreen viewChildScreen;
	private final SearchChildScreen searchChildScreen;
	private final ViewChildrenScreen viewChildrenScreen;

	public ChildController(ManageChildScreen manageChildScreen,
			ViewChildScreen viewChildScreen,
			SearchChildScreen searchChildScreen,
			ViewChildrenScreen viewChildrenScreen, UiStack uiStack,
			FormStore formStore, ChildStoreService childStoreService) {
		super(manageChildScreen, uiStack);
		this.manageChildScreen = manageChildScreen;
		this.viewChildScreen = viewChildScreen;
		this.formStore = formStore;
		this.childStoreService = childStoreService;
		this.searchChildScreen = searchChildScreen;
		this.viewChildrenScreen = viewChildrenScreen;
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();
	}

	public void newChild() {
		manageChildScreen.setForms(formStore.getForms());
		changeScreen(manageChildScreen);
	}

	public void editChild(Child child) {
		manageChildScreen.setEditForms(formStore.getForms(), child);
		changeScreen(manageChildScreen);
	}

	public void takeSnapshotAndUpdateWithNewImage(
			ImageCaptureListener imageCaptureListener) {

		SnapshotController snapshotController = new SnapshotController(
				new SnapshotScreen(), uiStack);
		snapshotController.show();
		snapshotController.setImageListener(imageCaptureListener);
	}

	public void saveChild(Child child) {
		childStoreService.saveChildInLocalStore(child);
	}

	public void syncChild(Child child) {
		dispatcher.syncChild(child);
	}

	public void viewChild(Child child) {
		viewChildScreen.setChild(child);
		changeScreen(viewChildScreen);
	}

	public void showHistory(Child child) {
		ChildHistoryScreen historyScreen = new ChildHistoryScreen(child);
		uiStack.pushScreen(historyScreen);
		historyScreen.setUp();
	}

	public void viewChildren() {
		Child[] children;
		children = childStoreService.getAllChildrenFromPhoneStoredAsArray();
		viewChildrenScreen.setChildren(children);
		changeScreen(viewChildrenScreen);
	}

	public void showChildSearchScreen() {
		changeScreen(searchChildScreen);
	}

	public void searchAndDispalyChildren(SearchChildFilter searchChildFilter) {
		Child children[] = childStoreService
				.searchChildrenFromStore(searchChildFilter);
		viewChildrenScreen.setChildren(children);
		changeScreen(viewChildrenScreen);
	}

}
