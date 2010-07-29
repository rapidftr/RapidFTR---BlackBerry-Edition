package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Vector;

import com.rapidftr.controllers.NewChildController;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.utilities.ImageCaptureListener;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class NewChildScreen extends CustomScreen {

	private Vector forms;
	private Manager screenManager;

	public void cleanUp() {

	}

	public void setUp() {

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				createScreenLayout();
			}
		});

	}

	public void setForms(Vector forms) {
		this.forms = forms;
		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			((Form) list.nextElement())
					.initializeLayout(this);
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
					"There are no form details stores\n"
							+ "press ok to synchronize forms with a server");

			if (result == Dialog.OK) {
				((NewChildController) controller).synchronizeForms();
			}
			controller.popScreen();
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

	}

	public void takePhoto(ImageCaptureListener imageCaptureListener) {

		((NewChildController) controller)
				.takeSnapshotAndUpdateWithNewImage(imageCaptureListener);

	}

}
