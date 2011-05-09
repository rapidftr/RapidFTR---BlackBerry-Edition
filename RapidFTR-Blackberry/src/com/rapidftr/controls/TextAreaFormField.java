package com.rapidftr.controls;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.form.FormField;
import com.rapidftr.utilities.Styles;

public class TextAreaFormField extends CustomField {

	private TextField textAreaField;

	public TextAreaFormField(final FormField field) {
		super(field);
		add(new LabelField(field.getDisplayName()));
		textAreaField = new BasicEditField();
		XYEdges edges = new XYEdges(10, 10, 30, 10);
		Border rounBorder = BorderFactory.createRoundedBorder(edges,
				Styles.COLOR_FIELD_BACKGROUND, Border.STYLE_SOLID);
		textAreaField.setBorder(rounBorder);
		add(textAreaField);
		textAreaField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field changedField, int context) {
				 setFieldValue(textAreaField.getText());
			}
		});
	}

	public void setValue(String value) {
		textAreaField.setText(value);
		setFieldValue(value);
	}
}
