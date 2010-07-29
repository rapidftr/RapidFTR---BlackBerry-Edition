package com.rapidftr.model;

import java.util.Vector;

public class FormFieldFactory {

	public FormField createFormField(String name, String type, Vector optionStrings) {

		FormField field = TextBoxField.createField(name, type);

		if (field != null)
			return field;

		field = SelectboxField.createdFormField(name, type, optionStrings);

		if (field != null)
			return field;

		field = RadioButtonField.createdFormField(name, type, optionStrings);

		if (field != null)
			return field;

		field = CheckboxField.createdFormField(name, type, optionStrings);

		if (field != null)
			return field;

		field = PhotoUploadField.createdFormField(name, type);

		if (field != null)
			return field;

		return null;

	}

}
