package com.rapidftr.controls;

import net.rim.device.api.ui.component.CheckboxField;

import com.rapidftr.form.FormField;

public class CheckboxFormField extends CustomField {
	
	private CheckboxField checkboxField;

	public CheckboxFormField(FormField field) {
		checkboxField = new CheckboxField(field.getDisplayName(), false);
		add(checkboxField);
	}
	
	public void setValue(String value) {
		checkboxField.setChecked("True".equalsIgnoreCase(value));		
	}
}
