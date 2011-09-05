package com.rapidftr.screens;

import net.rim.device.api.system.Characters;
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
import com.rapidftr.controls.FormFieldFactory;
import com.rapidftr.controls.UIForms;
import com.rapidftr.form.Forms;
import com.rapidftr.model.Child;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.DateFormatter;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.SaveDialog;

public class ManageChildScreen extends CustomScreen {

	private Forms forms;
	private Manager screenManager;
	private final DateFormatter dateFormatter;

	private String selectedTab;

	public ManageChildScreen(DateFormatter dateFormatter) {
		this.dateFormatter = dateFormatter;
	}

	public void setUp() {
		createScreenLayout();
	}

	public void setForms(Forms forms) {
		setForms(forms, null);
	}

	public void setForms(Forms forms, String selectedTab) {
		this.forms = forms;
		this.selectedTab = selectedTab;
	}

	private void createScreenLayout() {
		deleteScreenManager();

		screenManager = new VerticalFieldManager();
		screenManager.add(prepareTitleManager());
		screenManager.add(new SeparatorField());
		add(screenManager);

		final UIForms uiForms = new UIForms(forms, new FormFieldFactory(), getController().getChild());
		
		final Manager formManager = new HorizontalFieldManager(FIELD_LEFT);
		formManager.add(uiForms.getDefaultForm());

		final Manager formsManager = new HorizontalFieldManager(FIELD_HCENTER);
		final ObjectChoiceField availableForms = new ObjectChoiceField(
				"Choose form: ", uiForms.getFormNames());
		formsManager.add(availableForms);
		screenManager.add(formsManager);
		screenManager.add(new SeparatorField());
		screenManager.add(formManager);

		availableForms.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				formManager.deleteAll();
				formManager.add(uiForms.formAt(availableForms.getSelectedIndex()));
			}
		});

		availableForms.setSelectedIndex(uiForms.getIndexByName(selectedTab));
		screenManager.add(new BlankSeparatorField(15));

	}

	private Manager prepareTitleManager() {
		Manager titleManager = new HorizontalFieldManager(FIELD_HCENTER);
		titleManager.add(new LabelField(getController().getScreenTitle()));
		return titleManager;
	}

	private void deleteScreenManager() {
		try {
			delete(screenManager);
		} catch (Exception ignored) {
		}
	}

	public void takePhoto(ImageCaptureListener imageCaptureListener) {
		getController().takeSnapshotAndUpdateWithNewImage(imageCaptureListener);
	}

	public boolean onClose() {
		return new SaveDialog((ManageChildController)controller).show(this);
	}

	protected void onMainMenuClick() {
	    new SaveDialog((ManageChildController)controller).show(this);
	}

	abstract class ControllerAction {
		abstract void execute();
	}

	public ManageChildController getController() {
		return ((ManageChildController) controller);
	}


	protected void makeMenu(Menu menu, int instance) {
		if (forms.isNotEmpty()) {
			MenuItem saveChildMenu = new MenuItem("Save Child ", 1, 1) {
				public void run() {
					if (!getController().validateOnSave(forms, dateFormatter)) {
						return;
					}
					getController().viewChild();
					getController().setChild(null);
				}
			};
			menu.add(saveChildMenu);
		}

		addSyncFailedErrorMenuItem(menu);

		super.makeMenu(menu, instance);
	}

	private void addSyncFailedErrorMenuItem(Menu menu) {
		final Child childToEdit = getController().getChild();
		if (childToEdit != null && childToEdit.isSyncFailed()) {
			MenuItem syncFailesErrorMenu = new MenuItem("Sync Error ", 2, 2) {
				public void run() {
					Dialog.alert(childToEdit.childStatus().getSyncError());
				}
			};
			menu.add(syncFailesErrorMenu);
		}
	}

	public boolean keyChar(char key, int status, int time) {
		if (key == Characters.ESCAPE) {
			return onClose();
		}

		return super.keyChar(key, status, time);
	}

	public boolean keyDown(int keycode, int time) {
		if (keycode == Characters.ESCAPE) {
			return onClose();
		}
		return super.keyDown(keycode, time);
	}

	public Forms getForms() {
		return forms;
	}

	public DateFormatter getDateFormatter() {
		return dateFormatter;
	}
	

}
