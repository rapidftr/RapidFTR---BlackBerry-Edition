package com.rapidftr.model;

import com.rapidftr.screens.ManageChildScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.VerticalFieldManager;

import java.util.Enumeration;
import java.util.Vector;

public class RadioButtonFormField extends FormField {

	private RadioButtonGroup group;
	private VerticalFieldManager manager;
	private Vector optionStrings;

	public RadioButtonFormField(String name, String displayName, String type, Vector optionStrings, String helpText) {
		super(name, displayName, type, helpText);
		this.optionStrings = optionStrings;
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		group = new RadioButtonGroup();
		manager.add(new LabelField(displayLabel()));

		for (Enumeration list = optionStrings.elements(); list
				.hasMoreElements();) {
			String text = (String) list.nextElement();
			net.rim.device.api.ui.component.RadioButtonField radioButtonField = new net.rim.device.api.ui.component.RadioButtonField(
					text, group, false);
			manager.add(radioButtonField);
		}
	}

	public Manager getLayout() {
		return manager;
	}

	public String getValue() {
		return group.getSelectedIndex() >=0 ? (String) optionStrings.elementAt(group.getSelectedIndex()) : "";
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof RadioButtonFormField))
			return false;

		if (obj == this)
			return true;

		RadioButtonFormField radioButtonFormField = (RadioButtonFormField) obj;
		return name.equals(radioButtonFormField.name)
				&& optionStrings.equals(radioButtonFormField.optionStrings);
	}

	public String toString() {

		return name + ":" + type + ":" + optionStrings;
	}

	public void setValue(String value) {
		int selectedIndex = 0;
		for (Enumeration list = optionStrings.elements(); list
				.hasMoreElements();) {
			if (((String) list.nextElement()).equals(value))
				break;
			selectedIndex++;
		}
		if (selectedIndex < optionStrings.size()) {
			group.setSelectedIndex(selectedIndex);
		}

	}

}
