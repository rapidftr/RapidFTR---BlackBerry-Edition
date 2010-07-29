package com.rapidftr.model;

import java.util.Vector;

import com.rapidftr.screens.NewChildScreen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class CheckboxField extends FormField {

	private static final String TYPE = "check_box";
	private VerticalFieldManager manager;
	private Vector optionStrings;
	private ObjectChoiceField field;

	private CheckboxField(String name, Vector optionStrings) {
		super(name, TYPE);
		this.optionStrings = optionStrings;
	}

	public Manager getLayout() {

		return manager;
	}

	public static CheckboxField createdFormField(String name, String type,
			Vector optionStrings) {
		if (type.equals(TYPE)) {
			return new CheckboxField(name, optionStrings);
		}

		return null;

	}

	public void initializeLayout(NewChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		Object[] optionArray = new Object[optionStrings.size()];
		optionStrings.copyInto(optionArray);
		field = new ObjectChoiceField(name + ":", optionArray);
		manager.add(field);

	}

}
