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
import com.rapidftr.model.Forms;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.DateFormatter;
import com.rapidftr.utilities.ImageCaptureListener;

public class ManageChildScreen extends CustomScreen {

    private Forms forms;
    private Manager screenManager;
    private final DateFormatter dateFormatter;
    private Child childToEdit;

    private static String[] REQUIRED_FIELDS = { };
	private String selectedTab;

    public ManageChildScreen(DateFormatter dateFormatter) {
        this.dateFormatter = dateFormatter;
    }

    public void setUp() {
        createScreenLayout();
    }

    public void setForms(Forms forms) {
    	setForms(forms, null, null);
    }

    public void setForms(Forms forms, Child childToEdit, String selectedTab) {
        this.childToEdit = childToEdit;
        this.forms = forms;
		this.selectedTab = selectedTab;
		forms.initializeLayout(this,childToEdit);
     }

    private void createScreenLayout() {
        deleteScreenManager();

        screenManager = new VerticalFieldManager();
        screenManager.add(prepareTitleManager());
        screenManager.add(new SeparatorField());
        add(screenManager);

        final Form[] formArray = forms.toArray();

        final Manager formManager = new HorizontalFieldManager(FIELD_LEFT);
        formManager.add((formArray[0]).getLayout());


        final Manager formsManager = new HorizontalFieldManager(FIELD_HCENTER);
        final ObjectChoiceField availableForms = new ObjectChoiceField("Choose form: ", formArray);
		formsManager.add(availableForms);
        screenManager.add(formsManager);
        screenManager.add(new SeparatorField());
        screenManager.add(formManager);

        availableForms.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field field, int context) {
                formManager.deleteAll();
                formManager.add((formArray[availableForms.getSelectedIndex()]).getLayout());
            }
        });
        
		selectDefaultForm(formArray, availableForms);
        screenManager.add(new BlankSeparatorField(15));

    }

	private void selectDefaultForm(final Form[] formArray, final ObjectChoiceField availableForms) {
		for (int i = 0; i < formArray.length; i++) {
			if ((formArray[i]).toString().equals(selectedTab)) {
				availableForms.setSelectedIndex(i);
				break;
			}
		}
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

    public void takePhoto(ImageCaptureListener imageCaptureListener) {
        getController().takeSnapshotAndUpdateWithNewImage(imageCaptureListener);
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
            childToEdit = Child.create(forms, dateFormatter.getCurrentFormattedDateTime());
        } else {
            childToEdit.update(forms);
            childToEdit.setField(Child.LAST_UPDATED_KEY, dateFormatter.getCurrentFormattedDateTime());
        }

        String invalidDataField;
        if ((invalidDataField = validateRequiredFields()) != "") {
            return invalidDataField;
        }
        getController().saveChild(childToEdit);
        return null;
    }

	public boolean onClose() {
		return displayConfirmation(new ControllerAction() {
			void execute() {
				controller.popScreen();
			}
		});
	}
    
	protected void onMainMenuClick() {
		displayConfirmation(new ControllerAction() {

			void execute() {
				controller.homeScreen();

			}
		});
	}

	private boolean displayConfirmation(ControllerAction action) {
		if (forms.isNotEmpty()) {
			String menuMessage = "The current record has been changed. What do you want to do with these changes?";
			String[] menuChoices = { "Save", "Discard", "Cancel" };
			int defaultChoice = 0;
			int result = Dialog.ask(menuMessage, menuChoices, defaultChoice);

			switch (result) {
			case 0: {
				if (validateOnSave()) {
					action.execute();
					return true;
				}
				break;
			}
			case 1: {
				action.execute();
				break;
			}
			case 3: {
				break;
			}

			}
		} else {
			action.execute();
		}
		return true;
	}

	abstract class ControllerAction {
		abstract void execute();
	}
	
	private ManageChildController getController() {
		return ((ManageChildController) controller);
	}

    private String validateRequiredFields() {
    	StringBuffer invalidFields= new StringBuffer("");
        for (int i = 0; i < REQUIRED_FIELDS.length; i++) {
            if (childToEdit.getField(REQUIRED_FIELDS[i]) == null || childToEdit.getField(REQUIRED_FIELDS[i]).equals("")) {
                invalidFields.append(" ," + REQUIRED_FIELDS[i]);
            }
        }
        return invalidFields.toString();
    }

    protected void makeMenu(Menu menu, int instance) {
        if (forms.isNotEmpty()) {
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
