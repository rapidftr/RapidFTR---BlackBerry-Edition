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
	private final ChildHistoryScreen childHistoryScreen;
	private CustomScreen currentChildScreen;
	private ChildrenRecordStore childrenStore;

	public ChildController(ManageChildScreen manageChildScreen,
			ViewChildScreen viewChildScreen,
			SearchChildScreen searchChildScreen,
			ViewChildrenScreen viewChildrenScreen, UiStack uiStack,
			FormStore formStore, ChildrenRecordStore childrenStore,
			ChildPhotoScreen childPhotoScreen, ChildHistoryScreen childHistoryScreen) {

		super(manageChildScreen, uiStack);
		this.manageChildScreen = manageChildScreen;
		this.viewChildScreen = viewChildScreen;
		this.formStore = formStore;
		this.childrenStore = childrenStore;
		this.searchChildScreen = searchChildScreen;
		this.viewChildrenScreen = viewChildrenScreen;
		this.childPhotoScreen = childPhotoScreen;
		this.childHistoryScreen = childHistoryScreen;
		this.currentChildScreen = this.manageChildScreen;

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
		childHistoryScreen.setChild(child);
		changeScreen(childHistoryScreen);
	}

	public void viewChildren() {
		Child[] children = childrenStore.getAllAsArray();
		viewChildrenScreen.setChildren(children);
		changeScreen(viewChildrenScreen);
	}

	public void showChildSearchScreen() {
		changeScreen(searchChildScreen);
	}

	public void searchAndDisplayChildren(String searchQuery) {
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

	public void sortByRecentlyAdded() {
		childrenStore.attachSorter(new ChildSorter(new String[]{"created_at"}, false));
		viewChildren();
	}

	public void sortByRecentlyUpdated() {
		childrenStore.attachSorter(new ChildSorter(new String[]{"last_updated_at"}, false));
		viewChildren();
	}
	
}
