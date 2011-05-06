package com.rapidftr.controls;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.form.FormField;
import com.rapidftr.utilities.Styles;

public class TextboxFormField extends CustomField {

	protected BasicEditField editField;

	public TextboxFormField(final FormField field) {
		super(field);
		add(new LabelField(field.getDisplayName()));
		editField = new BasicEditField();
		editField.setBorder(getFieldBorder());
		add(editField);
		editField.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field changedField, int context) {
				setFieldValue(editField.getText());
			}
		});
	}

	private Border getFieldBorder() {
		return BorderFactory.createRoundedBorder(shape(), backgroundColor(),style());
	}

	private int style() {
		return Border.STYLE_SOLID;
	}

	private int backgroundColor() {
		return Styles.COLOR_FIELD_BACKGROUND;
	}

	private XYEdges shape() {
		return new XYEdges(10, 10, 10, 10);
	}

	public void setValue(String value) {
		if (value != null) {
			editField.setText(value);
			setFieldValue(value);
		}
	}
}
