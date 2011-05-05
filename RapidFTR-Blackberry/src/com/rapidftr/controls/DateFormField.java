package com.rapidftr.controls;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.component.DateField;

import com.rapidftr.form.FormField;

public class DateFormField extends CustomField {
	DateField dateField;

	public DateFormField(FormField field) {
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
		dateField = new DateField(field.getDisplayName(), Long.MIN_VALUE,
				dateFormat, DateField.DATE);
		dateField.setDate(null);
		add(dateField);
	}
	
	public void setValue(String value) {
        dateField.setDate(Long.MIN_VALUE);
        if (value == null || value.length() == 0) {
            return;
        }
        long httpDate = HttpDateParser.parse(value);
        if (httpDate == 0) {
            return;
        }
        dateField.setDate(httpDate);
    }
}