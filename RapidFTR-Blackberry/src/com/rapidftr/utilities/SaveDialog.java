package com.rapidftr.utilities;

import net.rim.device.api.ui.component.Dialog;

import com.rapidftr.controllers.internal.SaveDialogHandler;
import com.rapidftr.screens.ManageChildScreen;

public class SaveDialog {

    public static final String MENU_MESSAGE = "The current record has been changed. What do you want to do with these changes?";
	public static final String[] MENU_CHOICES = { "Save", "Discard", "Cancel" };
	public SaveDialogHandler saveDialogHandler;

	public SaveDialog(SaveDialogHandler saveDialogHandler) {
		this.saveDialogHandler = saveDialogHandler;
	}
	
	public boolean show(ManageChildScreen screen){
		if (screen.isDirty()) {
			int defaultChoice = 0;
			int result = Dialog.ask(MENU_MESSAGE, MENU_CHOICES, defaultChoice);

			switch (result) {
			case 0: {
				if (screen.getController().validateOnSave(screen.getForms(),screen.getDateFormatter())) {
					saveDialogHandler.onSave();
					return true;
				}
				break;
			}
			case 1: {
				saveDialogHandler.onDiscard();
				break;
			}
			case 3: {
				saveDialogHandler.onCancel();
				break;
			}

			}
		} else {
			saveDialogHandler.onDiscard();
		}
		return true;
	}
}
