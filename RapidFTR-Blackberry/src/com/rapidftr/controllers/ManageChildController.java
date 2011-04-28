package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.ImageCaptureListener;

public class ManageChildController extends Controller {

	private final FormStore store;
	private ChildrenRecordStore childRecordStore;

	public ManageChildController(ManageChildScreen screen, UiStack uiStack,
			FormStore store, ChildrenRecordStore childRecordStore) {
		super(screen, uiStack);
		this.store = store;
		this.childRecordStore = childRecordStore;
	}

	public void editChild(Child child, String selectedTab) {
		getManageChildScreen().setForms(store.getHigherForms(), child, selectedTab);
		show();
	}

	public void newChild() {
		getManageChildScreen().setForms(store.getHigherForms());
		show();
	}

	private ManageChildScreen getManageChildScreen() {
		return ((ManageChildScreen) currentScreen);
	}

	public void takeSnapshotAndUpdateWithNewImage(
			ImageCaptureListener imageCaptureListener) {

		SnapshotController snapshotController = new SnapshotController(
				new SnapshotScreen(), uiStack);
		snapshotController.show();
		snapshotController.setImageListener(imageCaptureListener);
	}

	public void saveChild(Child child) {
		childRecordStore.addOrUpdate(child);
	}

	public void viewChild(Child child){
		dispatcher.viewChild(child);
	}

}
