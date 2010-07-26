package com.rapidftr.model;

import java.util.Vector;


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

}
