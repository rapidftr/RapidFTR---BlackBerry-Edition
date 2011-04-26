package com.rapidftr.model;

import java.util.Vector;

public class FormFieldFactory {

	public static final String TEXT_FIELD = "text_field";
	public static final String SELECT_FIELD = "select_box";
	public static final String RADIO_FIELD = "radio_button";
	public static final String CHECKBOX_FIELD = "check_box";
	public static final String PHOTO_FIELD = "photo_upload_box";
	public static final String TEXTAREA_FIELD = "textarea";
	public static final String numeric_field = "numeric_field";
	public static final String date_field = "date_field";
	public static final String AUDIO_FIELD = "audio_upload_box";
	
	public FormField createFormField(String name, String displayName, String type, Vector optionStrings, String helpText) {
		if(TEXT_FIELD.equals(type)) {
			return new TextboxFormField(name, displayName, type, helpText);
		}
		else if (SELECT_FIELD.equals(type)){
			return new SelectboxFormField(name, displayName, type, optionStrings, helpText);
		}
		else if (RADIO_FIELD.equals(type)) {
			return new RadioButtonFormField(name, displayName, type, optionStrings, helpText);
		}
		else if (CHECKBOX_FIELD.equals(type)) {
			return new CheckboxFormField(name, displayName, type, helpText);
		}
		else if (PHOTO_FIELD.equals(type)) {
			return new PhotoUploadFormField(name, displayName, type, helpText);
		}
		else if (TEXTAREA_FIELD.equals(type)) {
			return new TextAreaFormField(name, displayName, type, helpText);
		}
		else if (numeric_field.equals(type)) {
			return new NumericTextboxFormField(name, displayName, type, helpText);
		}
		else if (date_field.equals(type)) {
			return new DateFormField(name, displayName, type, helpText);
		}
        else if (AUDIO_FIELD.equals(type)) {
            return AudioField.createdFormField(name, type, helpText);
        }
		else {
			return null;
		}
	}

}
