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

import com.rapidftr.controllers.SyncChildController;
import com.rapidftr.controls.Button;
import com.rapidftr.net.ScreenCallBack;

public class SyncChildScreen extends CustomScreen implements
		FieldChangeListener, ScreenCallBack {

	private static final XYEdges PADDING = new XYEdges(10, 10, 10, 10);
	private Button okButton;

	GaugeField uploadProgressBar;

	private Manager hButtonManager;
	private Button cancelButton;

	public SyncChildScreen() {
		layoutScreen();
	}

	private void layoutScreen() {
		try {
			LabelField labelField = new LabelField("Syncing Child Records");
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
		uploadProgressBar.setLabel("Syncing ..");
		uploadProgressBar.setValue(0);
		hButtonManager.deleteAll();
		hButtonManager.add(cancelButton);

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
					"Are you sure want to cacel Sync?");
			//uploadProgressBar.setValue(0);

			if (result == Dialog.YES) {
				uploadProgressBar.setValue(0);
				((SyncChildController) controller).cancelUpload();
				controller.popScreen();
				return;
			}

		}

	}

	public void cleanUp() {
		((SyncChildController) controller).cancelUpload();
	}

	public boolean onClose() {
		cleanUp();
		return super.onClose();
	}

	public void handleAuthenticationFailure() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				int result = Dialog.ask(Dialog.D_OK_CANCEL,
						"You are not logged in.\n Press ok to  login");
				uploadProgressBar.setValue(0);

				controller.popScreen();

				if (result == Dialog.OK) {
					((SyncChildController) controller).login();
					return;
				}

			}
		});

	}

	public void handleConnectionProblem() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				Dialog
						.alert("Connection Error.\n Please Check your Internet Connectivity?");
				controller.popScreen();

			}
		});

	}

	public void updateRequestProgress(final int progress) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				uploadProgressBar.setValue(progress);
			}
		});

	}

	public void onProcessComplete() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				uploadProgressBar.setLabel("Complete");
				uploadProgressBar.setValue(100);
				hButtonManager.deleteAll();
				hButtonManager.add(okButton);
			}
		});
	}

	public void onProcessFail() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				int result = Dialog.ask(Dialog.D_YES_NO,
						"Sync Failed.\n Do you want to Retry?");
				uploadProgressBar.setValue(0);

				if (result == Dialog.YES) {
					((SyncChildController) controller).syncAllChildRecords();
					return;
				}

			//	controller.popScreen();

			}
		});

	}

	public void setProgressMessage(String message) {
		uploadProgressBar.setLabel(message);
	}

}
