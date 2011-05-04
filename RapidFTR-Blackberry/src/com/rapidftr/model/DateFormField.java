package com.rapidftr.model;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.component.DateField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.form.FormField;

public class DateFormField extends VerticalFieldManager {
	DateField dateField;

	public DateFormField(FormField field) {
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
		dateField = new DateField(field.getDisplayName(), Long.MIN_VALUE,
				dateFormat, DateField.DATE);
		dateField.setDate(null);
		add(dateField);
	}
}