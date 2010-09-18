package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ChildHistoryScreen;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.ViewChildScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ChildStoreService;
import com.rapidftr.utilities.ImageCaptureListener;

public class ChildController extends Controller {

	private final FormStore formStore;
	private final ChildStoreService childStoreService;
	private ManageChildScreen manageChildScreen;
	private ViewChildScreen viewChildScreen;

	public ChildController(ManageChildScreen manageChildScreen,
			ViewChildScreen viewChildScreen, UiStack uiStack,
			FormStore formStore, ChildStoreService childStoreService) {
		super(manageChildScreen, uiStack);
		this.manageChildScreen = manageChildScreen;
		this.viewChildScreen = viewChildScreen;
		this.formStore = formStore;
		this.childStoreService = childStoreService;
	}

	public void synchronizeForms() {
		dispatcher.synchronizeForms();
	}

	public void newChild() {
		((ManageChildScreen) currentScreen).setForms(formStore.getForms());
		super.show();
	}

	public void editChild(Child child) {
		manageChildScreen.setEditForms(formStore.getForms(),
				child);
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
	
	public void showHistory(Child child)
	{
		ChildHistoryScreen historyScreen = new ChildHistoryScreen(child);
		uiStack.pushScreen(historyScreen);
		historyScreen.setUp();
	}

}
