package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.RichTextField;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.utilities.BoldRichTextField;

public class Tab implements FocusChangeListener {

	private Form form;
	private Child child;

	private TabLabelField label;
	private final TabBodyField body;

	private Vector observers = new Vector();
	private TabsField canvas;

	private Vector toBeIgnored = new Vector() {
		{
			addElement("histories");
			addElement("_id");
			addElement("current_photo_key");
		}
	};

	public Tab(String label, Form form, Child child) {
		this(form, child, new TabLabelField(label), new TabBodyField());
	}

	protected Tab(Form form, Child child, TabLabelField label,
			TabBodyField bodyField) {

		this.form = form;
		this.child = child;

		this.label = label;
		label.addFocusChangeListener(this);
		this.body = bodyField;
		render();
	}

	private void render() {
		form.forEachField(new FieldAction() {
			public void execute(FormField field) {
				String key = field.getName();
				if (!toBeIgnored.contains(key)) {
					child.updateField(key);
					drawField(body, field, child.getField(key));
				}
			}
		});
	}

	public void open() {
		label.select();
		canvas.setBody(body);
	}

	public void close() {
		label.deSelect();
		canvas.clearBody();
	}

	public void setCanvas(TabsField canvas) {
		this.canvas = canvas;
		this.canvas.addHandle(label);
	}

	public void addTabChangeListener(FocusChangeListener observer) {
		observers.addElement(observer);
	}

	private void drawField(final Manager renderingArea, FormField field,
			String value) {
		Field detail = null;
		if (isNotEmpty(value)) {
			detail = createEmptyRecord(field);
		} else {
			detail = createRecordWithValue(field, value);
		}
		renderingArea.add(detail);
		renderingArea.add(new SeparatorField());
	}

	private RichTextField createRecordWithValue(FormField field, String value) {
		return BoldRichTextField
				.getSemiBoldRichTextField(drawKey(field), value);
	}

	private LabelField createEmptyRecord(FormField field) {
		return new LabelField(drawKey(field), LabelField.FOCUSABLE) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.GRAY);
				super.paint(graphics);
			}
		};
	}

	private boolean isNotEmpty(String value) {
		return value.trim().equals("");
	}

	private String drawKey(FormField field) {
		return field.displayLabel() + " : ";
	}

	private void notifyObservers(Field field, int eventType) {
		for (int i = 0; i < observers.size(); i++) {
			((FocusChangeListener) observers.elementAt(i)).focusChanged(field,
					eventType);
		}
	}

	public void focusChanged(Field field, int eventType) {
		notifyObservers(field, eventType);
	}

	public String getLabel() {
		return label.getText().trim();
	}
}
