package com.rapidftr.model;

import com.rapidftr.form.FormField;

public class FormFieldFactory {

	public CustomField createFrom(FormField field) {
		if ("text_field".equals(field.getType())) {
			return new TextboxFormField(field);
		} else if ("select_box".equals(field.getType())) {
			return new SelectboxFormField(field);
		} else if ("radio_button".equals(field.getType())) {
			return new RadioButtonFormField(field);
		} else if ("check_box".equals(field.getType())) {
			return new CheckboxFormField(field);
		} else if ("photo_upload_box".equals(field.getType())) {
			return new PhotoUploadFormField(field);
		} else if ("textarea".equals(field.getType())) {
			return new TextAreaFormField(field);
		} else if ("numeric_field".equals(field.getType())) {
			return new NumericTextboxFormField(field);
		} else if ("date_field".equals(field.getType())) {
			return new DateFormField(field);
		} else if ("audio_upload_box".equals(field.getType())) {
			return new AudioField(field);
		} else {
			return new TextboxFormField(field);
		}
	}
}
