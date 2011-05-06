package com.rapidftr.controls;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;

import com.rapidftr.form.FormField;
import com.rapidftr.form.OptionAction;

public class RadioButtonFormField extends CustomField {

	private RadioButtonGroup group;

	public RadioButtonFormField(FormField field) {
		super(field);
		group = new RadioButtonGroup();
		add(new LabelField(field.getDisplayName()));
		addOptionsFrom(field);
	}

	private void addOptionsFrom(FormField field) {
		field.forEachOption(new OptionAction() {
			public void execute(String option) {
				final RadioButtonField radioButtonField = new RadioButtonField(
						option, group, false);
				radioButtonField.setChangeListener(new FieldChangeListener() {
					public void fieldChanged(Field changedField, int context) {
						setFieldValue(radioButtonField.getLabel());
					}
				});
				add(radioButtonField);
			}
		});
	}

	public void setValue(final String value) {
		field.forEachOption(new OptionAction() {
			int i = 0;

			public void execute(String option) {
				if (option.equals(value)) {
					group.setSelectedIndex(i);
				}
				i++;
			}
		});
		setFieldValue(value);
	}

}
