package com.rapidftr.screens;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.controllers.UploadChildrenRecordsController;
import com.rapidftr.controls.Button;

public class SyncAllScreen extends CustomScreen implements
		FieldChangeListener {

	private static final XYEdges PADDING = new XYEdges(10, 10, 10, 10);
	private Button okButton;

	GaugeField uploadProgressBar;

	private Manager hButtonManager;
	private Button cancelButton;

	public SyncAllScreen() {
		layoutScreen();
	}

	private void layoutScreen() {
		try {
			LabelField labelField = new LabelField("Uploading Child Records");
			Manager hManager = new HorizontalFieldManager(FIELD_HCENTER);
			hManager.add(labelField);
			hManager.setPadding(PADDING);

			add(hManager);

			Manager hGaugeManager = new HorizontalFieldManager(FIELD_HCENTER);

			uploadProgressBar = new GaugeField("", 0, 100, 0,
					GaugeField.LABEL_AS_PROGRESS);

			uploadProgressBar.setBorder(BorderFactory
					.createSimpleBorder(new XYEdges(1, 1, 1, 1)));

			hGaugeManager.add(uploadProgressBar);
			hGaugeManager.setPadding(PADDING);
			add(hGaugeManager);

			hButtonManager = new HorizontalFieldManager(FIELD_HCENTER);
			add(hButtonManager);
			hButtonManager.setPadding(PADDING);
			okButton = new Button("Ok");
			okButton.setChangeListener(this);
			cancelButton = new Button("Cancel");
			cancelButton.setChangeListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void resetProgressBar() {
		uploadProgressBar.setLabel("Uploading ..");
		uploadProgressBar.setValue(0);
		hButtonManager.deleteAll();
		hButtonManager.add(cancelButton);

	}

	public void updateUploadProgessBar(final int value) {

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				uploadProgressBar.setValue(value);
			}
		});

	}

	public void uploadCompleted() {

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				uploadProgressBar.setLabel("Complete");
				uploadProgressBar.setValue(100);
				hButtonManager.deleteAll();
				hButtonManager.add(okButton);
			}
		});

	}

	public void uploadFailed() {

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				int result = Dialog.ask(Dialog.D_YES_NO,
						"Upload Failed.\n Do you want to Retry?");
				uploadProgressBar.setValue(0);

				if (result == Dialog.YES) {
					((UploadChildrenRecordsController) controller)
							.uploadChildRecords();
					return;
				}

				controller.popScreen();

			}
		});

	}

	public void setUp() {

	}

	public void fieldChanged(Field field, int context) {

		if (field.equals(okButton)) {
			controller.popScreen();
			return;
		}
		if (field.equals(cancelButton)) {
			int result = Dialog.ask(Dialog.D_YES_NO,
					"Are you sure want to cacel uploading?");
			uploadProgressBar.setValue(0);

			if (result == Dialog.YES) {
				((UploadChildrenRecordsController) controller).cancelUpload();
				controller.popScreen();
				return;
			}

		}

	}

	public void authenticationFailure() {

		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				int result = Dialog.ask(Dialog.D_OK_CANCEL,
						"You are not logged in.\n Press ok to  login");
				uploadProgressBar.setValue(0);

				controller.popScreen();

				if (result == Dialog.OK) {
					((UploadChildrenRecordsController) controller).login();
					return;
				}

			}
		});

	}

	public void cleanUp() {
		((UploadChildrenRecordsController) controller).cancelUpload();
	}

	public boolean onClose() {
		cleanUp();
		return super.onClose();
	}

	public void connectionProblem() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog
						.alert("Connection Error.\n Please Check your Internet Connectivity?");
				controller.popScreen();

			}
		});

	}
}
