package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.utilities.BoldRichTextField;

public class Tab {
	private Form form;
	private Child child;
	private final String label;
	private Vector toBeIgnored = new Vector(){{
		addElement("histories");
		addElement("_id");
		addElement("current_photo_key");
	}};
	
	public Tab(String label, Form form, Child child) {
		this.form = form;
		this.child = child;
		this.label = label;
	}

	public void render(final Manager renderingArea) {
		form.forEachField(new FieldAction() {
			public void execute(FormField field) {
				String key = field.getName();
				if (toBeIgnored.contains(key)) {
					return;
				}
				child.updateField(key);
				String value = child.getField(key);
				drawField(renderingArea, field, value);
			}
		});

	}
	
	private void drawField(final Manager renderingArea, FormField field,
			String value) {
		Field detail = null;
		if (isNotEmpty(value)) {
			detail = new LabelField(drawKey(field), LabelField.FOCUSABLE) {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.GRAY);
					super.paint(graphics);
				}
			};
		} else {
			detail = BoldRichTextField.getSemiBoldRichTextField(drawKey(field),
					value);
		}
		renderingArea.add(detail);
		renderingArea.add(new SeparatorField());
	}

	private boolean isNotEmpty(String value) {
		return value.trim().equals("");
	}

	private String drawKey(FormField field) {
		return field.displayLabel() + " : ";
	}

	public String getLabel() {
		return label;
	}
}
