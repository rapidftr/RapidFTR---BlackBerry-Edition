package com.rapidftr.controls;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ObjectChoiceField;

import com.rapidftr.form.FormField;
import com.rapidftr.form.OptionAction;

public class SelectboxFormField extends CustomField {

	private ObjectChoiceField choiceField;

	public SelectboxFormField(final FormField field) {
		super(field, Field.FIELD_LEFT);
		initializeChoiceField(field);
		add(choiceField);
	}

	private void initializeChoiceField(final FormField field) {
		final String[] optionArray = field.getOptionsArray();
		if (optionArray[0] == "") {
			optionArray[0] = "...";
		}
		choiceField = createChoiceField(field.getDisplayName(), optionArray);
		choiceField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field changedField, int context) {
				setFieldValue(optionArray[choiceField.getSelectedIndex()]);
			}
		});
	}

	private ObjectChoiceField createChoiceField(String label,
			String[] optionArray) {
		return new ObjectChoiceField(label + ":", optionArray);
	}
	
	public void setValue(final String value) {
		field.forEachOption(new OptionAction() {
			int i = 0;
			public void execute(String option) {
				if(option.equals(value)){
					choiceField.setSelectedIndex(i);
				}
				i++;
			}
		});
		setFieldValue(value);
	}
}
