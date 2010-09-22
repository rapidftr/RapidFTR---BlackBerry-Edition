package com.rapidftr.model;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.utilities.Styles;

public class TextAreaFormField extends FormField {

	private VerticalFieldManager manager;
	private TextField field;

	public TextAreaFormField(String name, String type) {
		super(name, type);
	}

	public void initializeLayout(ManageChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		manager.add(new LabelField(name));
		field = new BasicEditField();
		XYEdges edges = new XYEdges(10, 10, 30, 10);
		Border rounBorder = BorderFactory.createRoundedBorder(edges,
				Styles.COLOR_FIELD_BACKGROUND, Border.STYLE_SOLID);
		field.setBorder(rounBorder);
		manager.add(field);
	}

	public Manager getLayout() {

		return manager;
	}

	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!(obj instanceof TextAreaFormField))
			return false;

		TextAreaFormField field = (TextAreaFormField) obj;

		return name.equals(field.name);
	}
	
	

	public String getValue() {
		return field.getText();
	}

	public void setValue(String value) {
		field.setText(value);		
	}
}
