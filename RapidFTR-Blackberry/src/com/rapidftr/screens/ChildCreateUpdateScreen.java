package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controllers.ChildCreateUpdateController;
import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.controls.Button;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.SettingsStore;

public class ChildCreateUpdateScreen extends CustomScreen {

	private Vector forms;
	private Manager screenManager;
	SettingsStore settings;

	public ChildCreateUpdateScreen(SettingsStore settings) {
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

	private Child childInEditMode;

	public void setEditForms(Vector forms, Child child) {
		this.childInEditMode = child;
		this.forms = forms;
		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			((Form) list.nextElement()).initializeLayoutWithChild(this, child);
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
				((ChildCreateUpdateController) controller).synchronizeForms();
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
				onSaveChildClicked();
				Dialog
						.alert("ChildRecord has been stored succesfully\n"
								+ "Please upload record to central server whenever you get Internet Access!!");
				controller.popScreen();
			}
		});
		saveButtonManager.add(saveButton);
		screenManager.add(saveButtonManager);

	}

	public void takePhoto(ImageCaptureListener imageCaptureListener) {

		((ChildCreateUpdateController) controller)
				.takeSnapshotAndUpdateWithNewImage(imageCaptureListener);

	}

	public boolean onClose() {

		int result = Dialog.ask(Dialog.D_SAVE);

		if (result == Dialog.SAVE) {
			onSaveChildClicked();
		}

		if (result == Dialog.CANCEL) {
			return false;
		}

		controller.popScreen();

		return true;
	}

	private void onSaveChildClicked() {
		Child child = new Child();
		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			Form form = (Form) list.nextElement();
			for (Enumeration fields = form.getFieldList().elements(); fields
					.hasMoreElements();) {
				FormField field = null;
				try {
					 field = (FormField) fields.nextElement();
					child.setField(field.getName(), field.getValue());
				} catch (Exception e) {
					System.out.print(field);
				}
			}
		}

		if (childInEditMode != null) {
			if (childInEditMode.getField("_id") != null) {
				child.setField("_id", childInEditMode.getField("_id"));
				childInEditMode = null;
			}
		} else {
			child.createUniqueId(settings.getCurrentlyLoggedIn());
		}
		((ChildCreateUpdateController) controller).saveChild(child);
	}

}
