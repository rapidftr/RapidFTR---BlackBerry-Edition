package com.rapidftr.controllers;

import com.rapidftr.controllers.internal.Controller;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.controllers.internal.SaveDialogHandler;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.form.Forms;
import com.rapidftr.model.Child;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.SnapshotScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.CustomDialog;
import com.rapidftr.utilities.DateFormatter;
import com.rapidftr.utilities.ImageCaptureListener;

public class ManageChildController extends Controller implements SaveDialogHandler {

	private final FormStore store;
	private ChildrenRecordStore childRecordStore;
	private Child childToEdit;
	private static String[] REQUIRED_FIELDS = {};
	CustomDialog customDialog = new CustomDialog();
	
	public ManageChildController(ManageChildScreen screen, UiStack uiStack,
			FormStore store, ChildrenRecordStore childRecordStore, Dispatcher dispatcher) {
		super(screen, uiStack, dispatcher);
		this.store = store;
		this.childRecordStore = childRecordStore;
	}

	public void editChild(Child child, String selectedTab) {
		childToEdit = child;
		getManageChildScreen().setForms(store.getForms(), selectedTab);
		show();
	}

	public void newChild() {
		getManageChildScreen().setForms(store.getForms());
		show();
	}

	private ManageChildScreen getManageChildScreen() {
		return ((ManageChildScreen) currentScreen);
	}

	public void takeSnapshotAndUpdateWithNewImage(
			ImageCaptureListener imageCaptureListener) {

		SnapshotController snapshotController = new SnapshotController(
				new SnapshotScreen(), uiStack, dispatcher, imageCaptureListener);
		snapshotController.show();
	}

	public void saveChild(Child child) {
		childRecordStore.addOrUpdate(child);
	}

	public void viewChild(){
		dispatcher.viewChild(childToEdit);
	}

	public void setChild(Child child){
		childToEdit = child;
	}
	public Child getChild() {
		return childToEdit;
	}
	
	public void onSave() {
		homeScreen();
	}

	public void onCancel() {
		// Don't do any thing
	}

	public void onDiscard() {
		homeScreen();
	}
	
	public String getScreenTitle() {
		if (childToEdit != null) {
			return "Edit Child Record";
		}
		return "Register Child";
	}
	
	public void setCustomDialog(CustomDialog customDialog){
		this.customDialog = customDialog;
	}
	private String onSaveChildClicked(Forms forms, DateFormatter dateFormatter) {
		if (childToEdit == null) {
			childToEdit = Child.create(forms, dateFormatter
					.getCurrentFormattedDateTime());
		} else {
			childToEdit.update(forms, dateFormatter.getCurrentFormattedDateTime());
			childToEdit.setField(Child.LAST_UPDATED_KEY, dateFormatter
					.getCurrentFormattedDateTime());
		}

		String invalidDataField;
		if (!(invalidDataField = validateRequiredFields()).equals("")) {
			return invalidDataField;
		}
		saveChild(childToEdit);
		return null;
	}
	
	public boolean validateOnSave(Forms forms, DateFormatter dateFormatter) {
		String invalidDataField = onSaveChildClicked(forms, dateFormatter);
		if (invalidDataField != null) {
			customDialog.alert("Please input the following mandatory field(s)"
					+ invalidDataField + " .");
			return false;
		}
		return true;
	}
	
	private String validateRequiredFields() {
		StringBuffer invalidFields = new StringBuffer("");
		for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
			if (childToEdit.getField(REQUIRED_FIELDS[i]) == null
					|| childToEdit.getField(REQUIRED_FIELDS[i]).equals("")) {
				invalidFields.append(" ," + REQUIRED_FIELDS[i]);
			}
		}
		return invalidFields.toString();
	}

}
