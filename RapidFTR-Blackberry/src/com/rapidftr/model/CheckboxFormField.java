package com.rapidftr.model;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.screens.NewChildScreen;

public class CheckboxFormField extends FormField {

	private static final String TYPE = "check_box";
	private VerticalFieldManager manager;
	private CheckboxField field;

	private CheckboxFormField(String name) {
		super(name, TYPE);
	}

	public Manager getLayout() {

		return manager;
	}

	public static CheckboxFormField createdFormField(String name, String type) {
		if (type.equals(TYPE)) {
			return new CheckboxFormField(name);
		}

		return null;

	}

	public void initializeLayout(NewChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		field = new CheckboxField(name, false);
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

}
