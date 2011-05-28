package com.rapidftr.controls;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.CheckboxField;

import com.rapidftr.form.FormField;

public class CheckboxFormField extends CustomField {
	
	private CheckboxField checkboxField;

	public CheckboxFormField(final FormField field) {
		super(field);
		checkboxField = new CheckboxField(field.getDisplayName(), false);
		add(checkboxField);
		checkboxField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field changedField, int context) {
				 setFieldValue(checkboxField.getChecked()+"");
			}
		});
	}
	
	public void setValue(String value) {
		checkboxField.setChecked("True".equalsIgnoreCase(value));		
		setFieldValue(value);
	}
}
