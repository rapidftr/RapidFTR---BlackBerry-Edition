package com.rapidftr.screens;

import com.rapidftr.controllers.SyncController;
import com.rapidftr.controls.Button;
import com.rapidftr.process.Process;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.services.ScreenCallBack;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.GaugeField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.decor.BorderFactory;

public class SyncScreen extends CustomScreen implements FieldChangeListener,
		ScreenCallBack {

	// Model
	Process process;

	private static final XYEdges PADDING = new XYEdges(10, 10, 10, 10);
	private Button okButton;
	private GaugeField downloadProgressBar;
	private Manager hButtonManager;
	private Button cancelButton;
	LabelField labelField;

	public SyncScreen() {
		layoutScreen();
	}

	private void layoutScreen() {
		try {
			labelField = new LabelField("Syncinging ...");
			Manager hManager = new HorizontalFieldManager(FIELD_HCENTER);
			hManager.add(labelField);
			hManager.setPadding(PADDING);
			add(hManager);
			Manager hGaugeManager = new HorizontalFieldManager(FIELD_HCENTER);
			downloadProgressBar = new GaugeField("", 0, 100, 0,
					GaugeField.LABEL_AS_PROGRESS);
			downloadProgressBar.setBorder(BorderFactory
					.createSimpleBorder(new XYEdges(1, 1, 1, 1)));
			hGaugeManager.add(downloadProgressBar);
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

	public void setUp() {
		labelField.setText(process.name());
		onProcessStart();
	}

	public void fieldChanged(Field field, int context) {

		if (field.equals(okButton)) {
			controller.popScreen();
			return;
		}
		if (field.equals(cancelButton)) {
			int result = Dialog.ask(Dialog.D_YES_NO,
					"Are you sure want to cancel " + process.name() + "?");

			if (result == Dialog.YES) {
				process.stopProcess();
				controller.popScreen();
				return;
			}

		}

	}

	public void onAuthenticationFailure() {
	}

	public void cleanUp() {
		process.stopProcess();
	}

	public boolean onClose() {
		cleanUp();
		return super.onClose();
	}

	public void onConnectionProblem() {
		onProcessFail("Connection Problem");
	}

	public void updateProgress(final int size) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				try{
				downloadProgressBar.setValue(size);
				}catch (Exception ignore) {
					// Gauge field throwing this wired exception if screen is not active
					// TODO handle this stuff by checking if current screen is active
				}
			}
		});
	}

	public void onProcessSuccess() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				downloadProgressBar.setLabel("Complete");
				downloadProgressBar.setValue(100);
				hButtonManager.deleteAll();
				hButtonManager.add(okButton);
			}
		});
	}

	public void setProgressMessage(String message) {
		downloadProgressBar.setLabel(message);
	}

	public void attachProcess(Process process) {
            this.process = process;
	}

	public void showRunninngProcessAlert() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				int result = Dialog.ask(Dialog.D_YES_NO, " other process [ "
						+ process.name()
						+ " ] is Still Runing .\n Do you want to Stop it?");
				if (result == Dialog.YES) {
						((SyncController) controller).clearProcess();
					return;
				}
			}
		});
	}

	public void onProcessFail(final String failureMessage) {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {

				String msg = "Attempt to sync failed.";
				if (null != failureMessage)
					msg = failureMessage;

				int result = Dialog.ask(Dialog.D_YES_NO, msg
						+ " \nDo you want to Retry?");
				downloadProgressBar.setValue(0);
				downloadProgressBar.setLabel("Failed");

				if (result == Dialog.YES) {
					process.startProcess();
					return;
				} else if (result == Dialog.NO) {
					controller.popScreen();
				}
			}
		});
	}

	public void onProcessStart() {
		UiApplication.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				if (process != null) {
					downloadProgressBar.setLabel(process.name() + " ...");
				}
				downloadProgressBar.setValue(0);
				hButtonManager.deleteAll();
				hButtonManager.add(cancelButton);

			}
		});
	}
	
	protected void makeMenu(Menu menu, int instance) {
		super.makeMenu(menu, instance);
	}

}
