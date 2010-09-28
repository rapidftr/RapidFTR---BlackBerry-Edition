package com.rapidftr.model;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.screens.ManageChildScreen;

public class CheckboxFormField extends FormField {

	private VerticalFieldManager manager;
	private CheckboxField field;

	public CheckboxFormField(String name, String displayName, String type) {
		super(name, displayName, type);
	}

	public Manager getLayout() {

		return manager;
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		field = new CheckboxField(displayLabel(), false);
		manager.add(field);
	}

	public String getValue() {

		return new Boolean(field.getChecked()).toString();
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof CheckboxFormField))
			return false;

		if (obj == this)
			return true;

		CheckboxFormField checkboxField = (CheckboxFormField) obj;

		return name.equals(checkboxField.name);
	}

	public void setValue(String value) {
		field.setChecked(value.equalsIgnoreCase("True"));		
	}

}
