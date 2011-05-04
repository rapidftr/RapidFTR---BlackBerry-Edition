package com.rapidftr.model;

import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.form.FormField;
import com.rapidftr.utilities.Styles;

public class TextboxFormField extends VerticalFieldManager {

	protected BasicEditField editField;

	public TextboxFormField(FormField field) {
		add(new LabelField(field.getDisplayName()));
		editField = new BasicEditField();
		setFieldBorders();
		add(editField);
	}

	private void setFieldBorders() {
		XYEdges edges = new XYEdges(10, 10, 10, 10);
		Border rounBorder = BorderFactory.createRoundedBorder(edges,
				Styles.COLOR_FIELD_BACKGROUND, Border.STYLE_SOLID);
		editField.setBorder(rounBorder);
	}
}
