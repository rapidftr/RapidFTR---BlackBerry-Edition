package com.rapidftr.controls;

import java.util.Date;

import net.rim.device.api.i18n.DateFormat;
import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.io.http.HttpDateParser;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.DateField;

import com.rapidftr.form.FormField;

public class DateFormField extends CustomField {
	DateField dateField;

	public DateFormField(FormField field) {
		super(field);
		DateFormat dateFormat = new SimpleDateFormat("d MMM yyyy");
		dateField = new DateField(field.getDisplayName(), Long.MIN_VALUE,
				dateFormat, DateField.DATE);
		dateField.setDate(null);
		add(dateField);
		dateField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				if (dateField.getDate() != Long.MIN_VALUE) {
					setFieldValue(new SimpleDateFormat("d MMM yyyy").format(new Date(dateField.getDate())));
		        }
			}
		});
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
        setFieldValue(value);
    }
}