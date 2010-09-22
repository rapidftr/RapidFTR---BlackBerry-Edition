package com.rapidftr.model;

import java.util.Vector;

public class FormFieldFactory {

	public static final String TEXT_FIELD = "text_field";
	public static final String SELECT_FIELD = "select_box";
	public static final String RADIO_FIELD = "radio_button";
	public static final String CHECKBOX_FIELD = "check_box";
	public static final String PHOTO_FIELD = "photo_upload_box";
	public static final String TEXTAREA_FIELD = "textarea";
	public static final String NUMERIC_TEXT_FIELD = "numeric_text_field";
	
	public FormField createFormField(String name, String type, Vector optionStrings) {

		if(TEXT_FIELD.equals(type)) {
			return new TextboxFormField(name, type);
		}
		else if (SELECT_FIELD.equals(type)){
			return new SelectboxFormField(name, type, optionStrings);
		}
		else if (RADIO_FIELD.equals(type)) {
			return new RadioButtonFormField(name, type, optionStrings);
		}
		else if (CHECKBOX_FIELD.equals(type)) {
			return new CheckboxFormField(name, type);
		}
		else if (PHOTO_FIELD.equals(type)) {
			return new PhotoUploadFormField(name, type);
		}
		else if (TEXTAREA_FIELD.equals(type)) {
			return new TextAreaFormField(name, type);
		}
		else if (NUMERIC_TEXT_FIELD.equals(type)) {
			return new NumericTextboxFormField(name, type);
		}
		else {
			return null;
		}
	}

}
