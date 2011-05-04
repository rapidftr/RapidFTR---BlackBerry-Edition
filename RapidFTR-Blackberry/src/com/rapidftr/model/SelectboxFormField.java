package com.rapidftr.model;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.form.FormField;

public class SelectboxFormField extends VerticalFieldManager {

	private ObjectChoiceField choiceField;

	public SelectboxFormField(FormField field) {
		super(Field.FIELD_LEFT);
		initializeChoiceField(field);
		add(choiceField);
	}

	private void initializeChoiceField(FormField field) {
		String[] optionArray = field.getOptionsArray();
		if (optionArray[0] == "") {
			optionArray[0] = "...";
		}
		choiceField = createChoiceField(field.getDisplayName(), optionArray);
	}

	private ObjectChoiceField createChoiceField(String label,
			String[] optionArray) {
		return new ObjectChoiceField(label + ":", optionArray);
	}
}
