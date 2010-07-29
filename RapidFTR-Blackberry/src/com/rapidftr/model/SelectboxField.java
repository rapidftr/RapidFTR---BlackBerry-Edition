package com.rapidftr.model;

import java.util.Vector;

import com.rapidftr.screens.NewChildScreen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class SelectboxField extends FormField {

	private static final String TYPE = "select_box";
	private Vector optionStrings;
	private VerticalFieldManager manager;
	private ObjectChoiceField field;

	private SelectboxField(String name, Vector optionStrings) {
		super(name, TYPE);
		this.optionStrings = optionStrings;
	}

	public void initializeLayout(NewChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		Object[] optionArray = new Object[optionStrings.size()];
		optionStrings.copyInto(optionArray);
		field = new ObjectChoiceField(name + ":", optionArray);
		manager.add(field);
	}

	public Manager getLayout() {

		return manager;
	}

	public static SelectboxField createdFormField(String name, String type,
			Vector optionStrings) {
		if (type.equals(TYPE)) {
			return new SelectboxField(name, optionStrings);
		}

		return null;

	}

}
