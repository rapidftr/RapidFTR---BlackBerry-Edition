package com.rapidftr.model;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.screens.NewChildScreen;
import com.rapidftr.utilities.Styles;

public class TextBoxField extends FormField {

	private static String TYPE = "text_field";
	private VerticalFieldManager manager;
	private BasicEditField field;

	private TextBoxField(String name) {
		super(name, TYPE);
	}

	public void initializeLayout(NewChildScreen newChildScreen) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		manager.add(new LabelField(name));
		field = new BasicEditField();
		XYEdges edges = new XYEdges(10, 10, 10, 10);
		Border rounBorder = BorderFactory.createRoundedBorder(edges,
				Styles.COLOR_FIELD_BACKGROUND, Border.STYLE_SOLID);
		field.setBorder(rounBorder);
		manager.add(field);
	}

	public Manager getLayout() {

		return manager;
	}

	public static TextBoxField createField(String name, String type) {

		if (type.equals(TYPE)) {
			return new TextBoxField(name);
		}

		return null;

	}

	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (!(obj instanceof TextBoxField))
			return false;

		TextBoxField field = (TextBoxField) obj;

		return name.equals(field.name);
	}

}
