package com.rapidftr.model;

import com.rapidftr.screens.ManageChildScreen;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import java.util.Enumeration;
import java.util.Vector;

public class SelectboxFormField extends FormField {

	private Vector optionStrings;
	private VerticalFieldManager manager;
	private ObjectChoiceField field;

	public SelectboxFormField(String name, String displayName, String type, Vector optionStrings) {
		super(name, displayName, type);
		this.optionStrings = optionStrings;
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		Object[] optionArray = new Object[optionStrings.size()];
		optionStrings.copyInto(optionArray);
        if (optionArray[0] == "")
            optionArray[0] = "...";
		field = new ObjectChoiceField(displayLabel() + ":", optionArray);
		manager.add(field);
	}

	public Manager getLayout() {
		return manager;
	}

	public String getValue() {
        return field.getSelectedIndex() >= 0 ? (String) optionStrings.elementAt(field.getSelectedIndex()) : "";

	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (!(obj instanceof SelectboxFormField))
			return false;

		if (obj == this)
			return true;

		SelectboxFormField selectboxFormField = (SelectboxFormField) obj;
		return name.equals(selectboxFormField.name)
				&& optionStrings.equals(selectboxFormField.optionStrings);
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
			field.setSelectedIndex(selectedIndex);
		}
	}

}
