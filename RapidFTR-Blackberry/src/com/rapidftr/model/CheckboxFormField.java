package com.rapidftr.model;

import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.form.FormField;

public class CheckboxFormField extends VerticalFieldManager {
	
	public CheckboxFormField(FormField field) {
		add(new CheckboxField(field.getDisplayName(), false));
	}
}
