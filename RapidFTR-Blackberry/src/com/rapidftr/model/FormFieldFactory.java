package com.rapidftr.model;

import java.util.Vector;

public class FormFieldFactory {

	public FormField createFormField(String name, String type, Vector optionStrings) {

		FormField field = TextboxFormField.createField(name, type);

		if (field != null)
			return field;

		field = SelectboxFormField.createdFormField(name, type, optionStrings);

		if (field != null)
			return field;

		field = RadioButtonFormField.createdFormField(name, type, optionStrings);

		if (field != null)
			return field;

		field = CheckboxFormField.createdFormField(name, type);

		if (field != null)
			return field;

		field = PhotoUploadFormField.createdFormField(name, type);

		if (field != null)
			return field;

		return null;

	}

}
