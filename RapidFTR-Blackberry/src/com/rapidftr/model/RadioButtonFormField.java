package com.rapidftr.model;

import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.form.FormField;
import com.rapidftr.form.OptionAction;

public class RadioButtonFormField extends VerticalFieldManager {

	private RadioButtonGroup group;

	public RadioButtonFormField(FormField field) {
		group = new RadioButtonGroup();
		add(new LabelField(field.getDisplayName()));
		addOptionsFrom(field);
	}

	private void addOptionsFrom(FormField field) {
		field.forEachOption(new OptionAction() {
			public void execute(String option) {
				RadioButtonField radioButtonField = new RadioButtonField(
						option, group, false);
				add(radioButtonField);
			}
		});
	}
}
