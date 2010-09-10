package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controllers.ManageChildController;
import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.controls.Button;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.SettingsStore;

public class ManageChildScreen extends CustomScreen {

	private Vector forms;
	private Manager screenManager;
	SettingsStore settings;
	private Child childToEdit;
	private Child childToSave;
	public ManageChildScreen(SettingsStore settings) {
		this.settings = settings;
	}

	public void cleanUp() {

	}

	public void setUp() {

		createScreenLayout();

	}

	public void setForms(Vector forms) {
		this.forms = forms;
		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			((Form) list.nextElement()).initializeLayout(this);
		}
	}


	public void setEditForms(Vector forms, Child childToEdit) {
		this.childToEdit = childToEdit;
		this.childToSave = childToEdit;
		this.forms = forms;
		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			((Form) list.nextElement()).initializeLayoutWithChild(this,
					childToEdit);
		}
	}

	private void createScreenLayout() {

		try {
			delete(screenManager);
		} catch (Exception ex) {

		}
		screenManager = new VerticalFieldManager();
		Manager titleManager = new HorizontalFieldManager(FIELD_HCENTER);
		LabelField screenTitle = new LabelField("Create New Child");
		titleManager.add(screenTitle);
		screenManager.add(titleManager);
		screenManager.add(new SeparatorField());
		add(screenManager);

		if (forms == null || forms.size() == 0) {
			int result = Dialog.ask(Dialog.D_OK_CANCEL,
					"There are no form details stored\n"
							+ "press ok to synchronize forms with a server");

			controller.popScreen();
			if (result == Dialog.OK) {
				((ManageChildController) controller).synchronizeForms();
			}
			return;
		}

		final Object[] formArray = new Object[forms.size()];
		forms.copyInto(formArray);
		final Manager formsManager = new HorizontalFieldManager(FIELD_HCENTER);
		final ObjectChoiceField availableForms = new ObjectChoiceField(
				"Choose form", formArray);
		formsManager.add(availableForms);
		screenManager.add(formsManager);

		final Manager formManager = new HorizontalFieldManager(FIELD_LEFT);
		formManager.add(((Form) formArray[0]).getLayout());
		screenManager.add(formManager);
		availableForms.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {

				formManager.deleteAll();
				formManager.add(((Form) formArray[availableForms
						.getSelectedIndex()]).getLayout());

			}

		});

		screenManager.add(new BlankSeparatorField(15));

		HorizontalFieldManager saveButtonManager = new HorizontalFieldManager(
				FIELD_HCENTER);
		Button saveButton = new Button("Save");
		saveButton.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
			
				int saveSuccess=onSaveChildClicked();
				if(saveSuccess == -1)
				{
					Dialog.alert("Please input last known loaction , it is a mandatory field");
					return;
				}
				childToEdit =  null;
				controller.popScreen();
			}
		});
		saveButtonManager.add(saveButton);
		screenManager.add(saveButtonManager);

	}

	public void takePhoto(ImageCaptureListener imageCaptureListener) {

		((ManageChildController) controller)
				.takeSnapshotAndUpdateWithNewImage(imageCaptureListener);

	}

	public boolean onClose() {
		int result = Dialog.ask(Dialog.D_YES_NO,"Do you want to save the changes before closing?",Dialog.YES);

		if (result == Dialog.YES) {
			int saveSuccess=onSaveChildClicked();
			if(saveSuccess==-1)
			{
				Dialog.alert("Please input last known loaction , it is a mandatory field");
				return false;
			}
		}

		if (result == Dialog.NO) {
			//Don't  do anything just exit
		}

		controller.popScreen();
		return true;
	}

	private int onSaveChildClicked() {
		if (childToEdit == null) {
			childToEdit = Child.create(forms);
		}else{
			childToEdit.update(settings.getCurrentlyLoggedIn(),forms);
		}
		if(areValidFieldsEmpty()){
			return -1;
		}
		((ManageChildController) controller).saveChild(childToEdit);
		return 0;
		//childToEdit =  null;
	}

	private boolean areValidFieldsEmpty() {
		return childToEdit.getField("last_known_location")==null || childToEdit.getField("last_known_location").toString().equals("");
	}
	
	protected void makeMenu(Menu menu, int instance) {
		MenuItem saveChildMenu = new MenuItem("Save Child ", 1, 1) {
			public void run() {
				int saveSuccess=onSaveChildClicked();
				if(saveSuccess==-1)
				{
					Dialog.alert("Please input last known loaction , it is a mandatory field");
					return;
				}
				Dialog.alert("ChildRecord has been stored succesfully\n"
								+ "Please upload record to central server whenever you get Internet Access!!");
				controller.popScreen();
				childToEdit =  null;
			}
		};
		
		MenuItem syncChildMenu = new MenuItem("Sync Record ", 2, 2) {
			public void run() {
				controller.popScreen();
				int saveSuccess=onSaveChildClicked();
				if(saveSuccess==-1)
				{
					Dialog.alert("Please input last known loaction , it is a mandatory field");
					return ;
				}
				((ManageChildController) controller).syncChild(childToEdit);
				childToEdit = null;
			}
		};

		MenuItem CloseMenu = new MenuItem("Close", 3, 1) {
			public void run() {
				onClose();
			}
		};
		
		menu.add(saveChildMenu);
		menu.add(syncChildMenu);
		menu.add(CloseMenu);
	}
	
	public Child getChild()
	{
		return childToEdit;
	}
}
