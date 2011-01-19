package com.rapidftr.screens;

import java.util.Enumeration;
import java.util.Vector;

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
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.Settings;

public class ManageChildScreen extends CustomScreen {

    private Vector forms;
    private Manager screenManager;
    Settings settings;
    private Child childToEdit;

    private static String[] REQUIRED_FIELDS = { };

    public ManageChildScreen(Settings settings) {
        this.settings = settings;
    }

    public void setUp() {
        createScreenLayout();
    }

    public void setForms(Vector forms) {
    	 childToEdit = null;
        this.forms = forms;
        for (Enumeration list = forms.elements(); list.hasMoreElements();) {
            ((Form) list.nextElement()).initializeLayout(this);
        }
    }

    public void setEditForms(Vector forms, Child childToEdit) {
        this.childToEdit = childToEdit;
        this.forms = forms;
        for (Enumeration list = forms.elements(); list.hasMoreElements();) {
            ((Form) list.nextElement()).initializeLayoutWithChild(this, childToEdit);
        }
    }

	private boolean formsEmpty() {
        for (Enumeration e = forms.elements(); e.hasMoreElements();) {
            Object nextElement = e.nextElement();
            if (nextElement != null) {
                Form form = (Form) nextElement;
                if (!form.isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private void createScreenLayout() {
        deleteScreenManager();

        screenManager = new VerticalFieldManager();
        screenManager.add(prepareTitleManager());
        screenManager.add(new SeparatorField());
        add(screenManager);

        final Object[] formArray = formsInArray();

        final Manager formManager = new HorizontalFieldManager(FIELD_LEFT);
        formManager.add(((Form) formArray[0]).getLayout());


        final Manager formsManager = new HorizontalFieldManager(FIELD_HCENTER);
        final ObjectChoiceField availableForms = new ObjectChoiceField("Choose form: ", formArray);
		formsManager.add(availableForms);
        screenManager.add(formsManager);
        screenManager.add(new SeparatorField());
        screenManager.add(formManager);

        availableForms.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                formManager.deleteAll();
                formManager.add(((Form) formArray[availableForms.getSelectedIndex()]).getLayout());
            }
        });

        screenManager.add(new BlankSeparatorField(15));

    }

    private Object[] formsInArray() {
		final Object[] formArray = new Object[forms.size()];
        forms.copyInto(formArray);
		return formArray;
	}

	private Manager prepareTitleManager() {
		Manager titleManager = new HorizontalFieldManager(FIELD_HCENTER);
        titleManager.add(new LabelField(getScreenTitle()));
		return titleManager;
	}

    private String getScreenTitle() {
        if (childToEdit != null) {
            return "Edit Child Record";
        }
        return "Register Child";
    }

    private void deleteScreenManager() {
		try {
            delete(screenManager);
        } catch (Exception ignored) {

        }
	}

    public boolean confirmOverWriteAudio() {
            return Dialog.ask(Dialog.D_YES_NO,
                    "This will overwrite previously recorded audio. Are you sure?") == Dialog.YES;
        }


    public void takePhoto(ImageCaptureListener imageCaptureListener) {
        getController().takeSnapshotAndUpdateWithNewImage(imageCaptureListener);
    }

	public boolean onClose() {
		if (!formsEmpty()) {
			String menuMessage = "The current record has been changed. What do you want to do with these changes?";
			String[] menuChoices = { "Save", "Discard", "Cancel" };
			int defaultChoice = 0;
			int result = Dialog.ask(menuMessage, menuChoices, defaultChoice);

			switch (result) {
			case 0: {
				if (validateOnSave()) {
					controller.popScreen();
					return true;
				}
				break;
			}
			case 1: {
				controller.popScreen();
				break;
			}
			case 3: {
				break;
			}

			}
		} else {
            controller.popScreen();
        }
		return true;
	}

	private boolean validateOnSave() {
        String invalidDataField = onSaveChildClicked();
        if (invalidDataField != null) {
            Dialog.alert("Please input the following mandatory field(s)" + invalidDataField + " .");
            return false;
        }
        return true;
    }

    private String onSaveChildClicked() {
        if (childToEdit == null) {
            childToEdit = Child.create(forms);
        } else {
            childToEdit.update(settings.getCurrentlyLoggedIn(), forms);
        }

        String invalidDataField;
        if ((invalidDataField = validateRequiredFields()) != "") {
            return invalidDataField;
        }
        getController().saveChild(childToEdit);
        return null;
    }

	private ManageChildController getController() {
		return ((ManageChildController) controller);
	}

    private String validateRequiredFields() {
    	StringBuffer invalidFields= new StringBuffer("");
        for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
            if (childToEdit.getField(REQUIRED_FIELDS[i]) == null || childToEdit.getField(REQUIRED_FIELDS[i]).toString().equals("")) {
                invalidFields.append(" ," + REQUIRED_FIELDS[i]);
            }
        }
        return invalidFields.toString();
    }

    protected void makeMenu(Menu menu, int instance) {
        if (!formsEmpty()) {
            MenuItem saveChildMenu = new MenuItem("Save Child ", 1, 1) {
                public void run() {
                    if (!validateOnSave()) {
                        return;
                    }
                    getController().viewChild(childToEdit);
                    childToEdit = null;
                }
            };
            menu.add(saveChildMenu);
        }

        addSyncFailedErrorMenuItem(menu);

        super.makeMenu(menu, instance);
    }

	private void addSyncFailedErrorMenuItem(Menu menu) {
		if(childToEdit!=null && childToEdit.isSyncFailed()){
       	 MenuItem syncFailesErrorMenu = new MenuItem("Sync Error ", 2, 2) {
                public void run() {
                    Dialog.alert(childToEdit.childStatus().getSyncError());
                }
            };
            menu.add(syncFailesErrorMenu);
        }
	}

    public Child getChild() {
        return childToEdit;
    }
    
	public boolean keyChar( char key, int status, int time ) 
    {
        if ( key == Characters.ESCAPE ) 
        {
        	return onClose();
        }
        
        return super.keyChar(key, status, time);
    }

    
    public boolean keyDown(int keycode, int time) 
    {
		if (keycode == Characters.ESCAPE) {
			return onClose();
		}
		return super.keyDown(keycode, time);
    }

}
