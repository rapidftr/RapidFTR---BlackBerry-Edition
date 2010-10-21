package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.datastore.ChildSorter;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ChildHistoryScreen;
import com.rapidftr.screens.ChildPhotoScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SearchChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.ViewChildrenScreen;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;

public class ChildController extends Controller {

	private final FormStore formStore;
	private final ManageChildScreen manageChildScreen;
	private final ViewChildScreen viewChildScreen;
	private final SearchChildScreen searchChildScreen;
	private final ViewChildrenScreen viewChildrenScreen;
	private final ChildPhotoScreen childPhotoScreen;
	private CustomScreen currentChildScreen;
	private ChildrenRecordStore childrenStore;

	public ChildController(ManageChildScreen manageChildScreen,
			ViewChildScreen viewChildScreen,
			SearchChildScreen searchChildScreen,
			ViewChildrenScreen viewChildrenScreen, UiStack uiStack,
			FormStore formStore, ChildrenRecordStore childrenStore,
			ChildPhotoScreen childPhotoScreen) {

		super(manageChildScreen, uiStack);
		this.manageChildScreen = manageChildScreen;
		this.viewChildScreen = viewChildScreen;
		this.formStore = formStore;
		this.childrenStore = childrenStore;
		this.searchChildScreen = searchChildScreen;
		this.viewChildrenScreen = viewChildrenScreen;
		this.childPhotoScreen = childPhotoScreen;
		this.currentChildScreen = this.manageChildScreen;

		childrenStore.attachSorter(new ChildSorter(new String[] { "name",
				"last_known_location" }));
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

	public void changeScreen(CustomScreen screen) {
		currentChildScreen = screen;
		currentChildScreen.setController(this);
		show();
	}

	public void changeBackToScreen(CustomScreen screen) {
		currentChildScreen = screen;
	}

	public void show() {
		if (!currentChildScreen.isActive())
			uiStack.pushScreen(currentChildScreen);
		currentChildScreen.setUp();
	}

	public void takeSnapshotAndUpdateWithNewImage(
			ImageCaptureListener imageCaptureListener) {

		SnapshotController snapshotController = new SnapshotController(
				new SnapshotScreen(), uiStack);
		snapshotController.show();
		snapshotController.setImageListener(imageCaptureListener);
	}

	public void saveChild(Child child) {
		childrenStore.addOrUpdate(child);
	}

	public void syncChild(Child child) {
		dispatcher.syncChild(child);
	}

	public void viewChild(Child child) {
		viewChildScreen.setChild(child);
		changeScreen(viewChildScreen);
	}

	public void viewChildPhoto(Child child) {
		childPhotoScreen.setChild(child);
		changeScreen(childPhotoScreen);
	}

	public void showHistory(Child child) {
		ChildHistoryScreen historyScreen = new ChildHistoryScreen(child);
		uiStack.pushScreen(historyScreen);
		historyScreen.setUp();
	}

	public void viewChildren() {
		Child[] children = childrenStore.getAllAsArray();
		viewChildrenScreen.setChildren(children);
		changeScreen(viewChildrenScreen);
	}

	public void showChildSearchScreen() {
		changeScreen(searchChildScreen);
	}

	public void searchAndDispalyChildren(String searchQuery) {
		Child[] children = childrenStore.search(searchQuery);
		if (children.length != 0) {
			viewChildrenScreen.setChildren(children);
			changeScreen(viewChildrenScreen);
		} else {
			searchChildScreen.showNoSearchResultsAlert();
		}
	}

	public void popScreen() {
		currentChildScreen.popScreen(uiStack, this);
	}
}
