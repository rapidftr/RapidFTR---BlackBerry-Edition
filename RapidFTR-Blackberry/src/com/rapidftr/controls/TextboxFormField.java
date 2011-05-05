package com.rapidftr.controls;

import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.form.FormField;
import com.rapidftr.utilities.Styles;

public class TextboxFormField extends CustomField {

	protected BasicEditField editField;

	public TextboxFormField(FormField field) {
		add(new LabelField(field.getDisplayName()));
		editField = new BasicEditField();
		editField
		.setBorder(BorderFactory.createRoundedBorder(new XYEdges(10,
				10, 10, 10),
		Styles.COLOR_FIELD_BACKGROUND, Border.STYLE_SOLID));
		add(editField);
	}

	public void setValue(String value) {
		if (value != null)
			editField.setText(value);
	}
}
