package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Vector;

import com.rapidftr.screens.ManageChildScreen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class SelectboxFormField extends FormField {

	private Vector optionStrings;
	private VerticalFieldManager manager;
	private ObjectChoiceField field;

	public SelectboxFormField(String name, String type, Vector optionStrings) {
		super(name, type);
		this.optionStrings = optionStrings;
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		Object[] optionArray = new Object[optionStrings.size()];
		optionStrings.copyInto(optionArray);
		field = new ObjectChoiceField(name + ":", optionArray);
		manager.add(field);
	}

	public Manager getLayout() {
		return manager;
	}

	public String getValue() {

		int selectedIndex = field.getSelectedIndex();
		if(selectedIndex >=0)
		{
			return (String) optionStrings.elementAt(selectedIndex);
		}
		else
		{
			return "";
		}

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
