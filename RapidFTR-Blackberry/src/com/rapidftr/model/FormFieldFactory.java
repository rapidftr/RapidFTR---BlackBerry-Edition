package com.rapidftr.model;

import net.rim.device.api.ui.Field;

import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.form.FormField;

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

	public Field createFrom(FormField field) {
		if (TEXT_FIELD.equals(field.getType())) {
			return new TextboxFormField(field);
		} else if (SELECT_FIELD.equals(field.getType())) {
			return new SelectboxFormField(field);
		} else if (RADIO_FIELD.equals(field.getType())) {
			return new RadioButtonFormField(field);
		} else if (CHECKBOX_FIELD.equals(field.getType())) {
			return new CheckboxFormField(field);
		} else if (PHOTO_FIELD.equals(field.getType())) {
			return new PhotoUploadFormField(field);
		} else if (TEXTAREA_FIELD.equals(field.getType())) {
			return new TextAreaFormField(field);
		}else if (numeric_field.equals(field.getType())) {
			return new NumericTextboxFormField(field);
		}else if (date_field.equals(field.getType())) {
			return new DateFormField(field);
		}else if (AUDIO_FIELD.equals(field.getType())) {
			return new AudioField(field);
		} else {
			return new BlankSeparatorField(0);
		}

	}

}
