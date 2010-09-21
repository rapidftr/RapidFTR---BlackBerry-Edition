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

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.controls.Button;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.ImageCaptureListener;
import com.rapidftr.utilities.SettingsStore;

public class ManageChildScreen extends CustomScreen {

    private Vector forms;
    private Manager screenManager;
    SettingsStore settings;
    private Child childToEdit;

    private static String[] REQUIRED_FIELDS = { "last_known_location", "current_photo_key" };

    public ManageChildScreen(SettingsStore settings) {
        this.settings = settings;
    }

    public void cleanUp() {

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
            int result = Dialog.ask(Dialog.D_OK_CANCEL, "There are no form details stored\n" + "press ok to synchronize forms with a server");

            controller.popScreen();
            if (result == Dialog.OK) {
                ((ChildController) controller).synchronizeForms();
            }
            return;
        }

        final Object[] formArray = new Object[forms.size()];
        forms.copyInto(formArray);
        final Manager formsManager = new HorizontalFieldManager(FIELD_HCENTER);
        final ObjectChoiceField availableForms = new ObjectChoiceField("Choose form", formArray);
        formsManager.add(availableForms);
        screenManager.add(formsManager);

        final Manager formManager = new HorizontalFieldManager(FIELD_LEFT);
        formManager.add(((Form) formArray[0]).getLayout());
        screenManager.add(formManager);
        availableForms.setChangeListener(new FieldChangeListener() {

            public void fieldChanged(Field field, int context) {
                formManager.deleteAll();
                formManager.add(((Form) formArray[availableForms.getSelectedIndex()]).getLayout());

            }

        });

        screenManager.add(new BlankSeparatorField(15));

        HorizontalFieldManager saveButtonManager = new HorizontalFieldManager(FIELD_HCENTER);
        Button saveButton = new Button("Save");
        saveButton.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                if (!validateOnSave())
                    return;
                childToEdit = null;
                controller.popScreen();
            }
        });
        saveButtonManager.add(saveButton);
        screenManager.add(saveButtonManager);

    }

    public void takePhoto(ImageCaptureListener imageCaptureListener) {

        ((ChildController) controller).takeSnapshotAndUpdateWithNewImage(imageCaptureListener);

    }

    public boolean onClose() {
        int result = Dialog.ask(Dialog.D_YES_NO, "Do you want to save the changes before closing?", Dialog.YES);

        if (result == Dialog.YES) {
            if (!validateOnSave())
                return false;
        }

        if (result == Dialog.NO) {
            // Don't do anything just exit
        }

        controller.popScreen();
        return true;
    }

    private boolean validateOnSave() {
        String invalidDataField = onSaveChildClicked();
        if (invalidDataField != null) {
            Dialog.alert("Please input " + invalidDataField + " it is a mandatory field");
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
        String invalidDataField = null;
        if ((invalidDataField = validateRequiredFields()) != null) {
            return invalidDataField;
        }
        ((ChildController) controller).saveChild(childToEdit);
        return null;
    }

    private String validateRequiredFields() {
        for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
            if (childToEdit.getField(REQUIRED_FIELDS[i]) == null || childToEdit.getField(REQUIRED_FIELDS[i]).toString().equals(""))
                return REQUIRED_FIELDS[i];
        }
        return null;
    }

    protected void makeMenu(Menu menu, int instance) {
        MenuItem saveChildMenu = new MenuItem("Save Child ", 1, 1) {
            public void run() {
                if (!validateOnSave())
                    return;
                Dialog.alert("ChildRecord has been stored succesfully\n" + "Please upload record to central server whenever you get Internet Access!!");
                controller.popScreen();
                childToEdit = null;
            }
        };

        MenuItem syncChildMenu = new MenuItem("Sync Record ", 2, 2) {
            public void run() {
            
                if (!validateOnSave())
                    return;
                ((ChildController) controller).syncChild(childToEdit);
                childToEdit = null;
                controller.popScreen();
            }
        };

        if(childToEdit!=null && childToEdit.isSyncFailed()){
        	 MenuItem syncFailesErrorMenu = new MenuItem("Sync Error ", 2, 2) {
                 public void run() {
                     Dialog.alert(childToEdit.childStatus().getSyncError());
                     
                 }
             };

        }
        
        MenuItem CloseMenu = new MenuItem("Close", 3, 1) {
            public void run() {
                onClose();
            }
        };

        menu.add(saveChildMenu);
        menu.add(syncChildMenu);
        menu.add(CloseMenu);
    }

    public Child getChild() {
        return childToEdit;
    }
}
