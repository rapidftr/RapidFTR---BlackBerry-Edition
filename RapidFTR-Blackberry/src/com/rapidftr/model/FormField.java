package com.rapidftr.model;

import java.util.Vector;


import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;


public class FormField {

	protected String name;
	protected String type;
	private final Vector optionStrings;

	public FormField(String name, String type) {
		this.name = name;
		this.type = type;
		this.optionStrings = new Vector();
	}

	public FormField(String name, String type, Vector optionStrings) {
		this.name = name;
		this.type = type;
		this.optionStrings = optionStrings;
	}

	public String toString() {
		return name + " : " + type + " : "  + optionStrings.toString();
	}

	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof FormField))
			return false;

		FormField formField = (FormField) obj;

		return name.equals(formField.name) && type.equals(formField.type)&& optionStrings.equals(formField.optionStrings);

	}

	public Manager getLayout() {
		
		Manager manager = new HorizontalFieldManager(Field.FIELD_LEFT);
		if(type .equals("text_field"))
		{	
			manager.add(new EditField(name+":",""));
		}
		if(type .equals("select_box"))
		{	
			Object[] optionArray = new Object[optionStrings.size()];
			optionStrings.copyInto(optionArray);
			manager.add(new ObjectChoiceField(name+":",optionArray));
		}
		if(type.equals("check_box"))
		{
			manager.add(new CheckboxField(name+":",false));
		}
		
		
		
		
		return manager;
		
	}

}
