package com.rapidftr.controls;

import net.rim.device.api.ui.text.NumericTextFilter;

import com.rapidftr.form.FormField;

public class NumericTextboxFormField extends TextboxFormField {
	public NumericTextboxFormField(FormField field) {
		super(field);
		editField.setFilter(new NumericTextFilter());
	}
}
