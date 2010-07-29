package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Vector;

import com.rapidftr.screens.NewChildScreen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class RadioButtonField extends FormField {

	private static final String TYPE = "radio_button";
	private RadioButtonGroup group;
	private VerticalFieldManager manager;
	private Vector optionStrings;

	private RadioButtonField(String name, Vector optionStrings) {
		super(name, TYPE);
		this.optionStrings = optionStrings;
	}

	public void initializeLayout(NewChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		group = new RadioButtonGroup();
		manager.add(new LabelField(name));

		for (Enumeration list = optionStrings.elements(); list
				.hasMoreElements();) {
			String text = (String) list.nextElement();
			net.rim.device.api.ui.component.RadioButtonField radioButtonField = new net.rim.device.api.ui.component.RadioButtonField(
					text, group, false);
			manager.add(radioButtonField);
		}
		group.setSelectedIndex(0);
	}

	public Manager getLayout() {
		return manager;
	}

	public static RadioButtonField createdFormField(String name, String type,
			Vector optionStrings) {
		if (type.equals(TYPE)) {
			return new RadioButtonField(name, optionStrings);
		}

		return null;

	}

}
