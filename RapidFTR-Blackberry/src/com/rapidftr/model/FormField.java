package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Vector;

import com.rapidftr.controllers.NewChildController;
import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.controls.Button;
import com.rapidftr.utilities.Styles;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.RadioButtonField;
import net.rim.device.api.ui.component.RadioButtonGroup;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

public class FormField {

	protected String name;
	protected String type;
	private final Vector optionStrings;
	private Manager manager;

	public FormField(String name, String type) {
		this.name = name;
		this.type = type;
		this.optionStrings = new Vector();
	}

	public FormField(String name, String type, Vector optionStrings) {
		this.name = name;
		this.type = type;
		this.optionStrings = optionStrings;
	}

	public String toString() {
		return name + " : " + type + " : " + optionStrings.toString();
	}

	public boolean equals(Object obj) {

		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof FormField))
			return false;

		FormField formField = (FormField) obj;

		return name.equals(formField.name) && type.equals(formField.type)
				&& optionStrings.equals(formField.optionStrings);

	}

	public void initializeLayout(final NewChildController controller) {
		manager = new VerticalFieldManager(Field.FIELD_LEFT);
		if (type.equals("text_field")) {
			manager.add(new LabelField(name));
			BasicEditField bevelBorderEdit = new BasicEditField();
			XYEdges edges = new XYEdges(10, 10, 10, 10);
			Border rounBorder = BorderFactory.createRoundedBorder(edges,
					Styles.COLOR_FIELD_BACKGROUND, Border.STYLE_SOLID);
			bevelBorderEdit.setBorder(rounBorder);

			manager.add(bevelBorderEdit);
		}
		if (type.equals("select_box")) {
			Object[] optionArray = new Object[optionStrings.size()];
			optionStrings.copyInto(optionArray);
			manager.add(new ObjectChoiceField(name + ":", optionArray));
		}
		if (type.equals("check_box")) {
			manager.add(new CheckboxField(name + ":", false));
		}
		if (type.equals("radio_button")) {
			RadioButtonGroup group = new RadioButtonGroup();
			manager.add(new LabelField(name));
			for (Enumeration list = optionStrings.elements(); list
					.hasMoreElements();) {
				String text = (String) list.nextElement();

				manager.add(new RadioButtonField(text, group, false));
			}
		}
		if(type.equals("photo_upload_box")){
			Button capturePhoto = new Button("capturePhoto");
			capturePhoto.setChangeListener(new FieldChangeListener() {
				
				public void fieldChanged(Field field, int context) {
					controller.takeSnapshot();
					
				}
				
			});
			manager.add(new LabelField(name));
			manager.add(capturePhoto);
		}
		
	}

	public Manager getLayout() {

		return manager;

	}

}
