package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.utilities.BoldRichTextField;

public class Tab extends VerticalFieldManager implements FocusChangeListener, Observable {

	private Form form;
	private Child child;
	private final String label;
	
	private TabHandleField handle;
	private final TabBodyField body;
	
	private Vector observers = new Vector();
	private TabControlField canvas;

	private Vector toBeIgnored = new Vector() {
		{
			addElement("histories");
			addElement("_id");
			addElement("current_photo_key");
		}
	};

	public Tab(String label, Form form, Child child) {
		this.form = form;
		this.child = child;
		this.label = label;

		handle = new TabHandleField(getLabel());
		handle.setFocusListener(this);
		body = new TabBodyField();

		render();
	}

	private void render() {
		final Manager renderingArea = body;
		form.forEachField(new FieldAction() {
			public void execute(FormField field) {
				String key = field.getName();
				if (!toBeIgnored.contains(key)) {
					child.updateField(key);
					drawField(renderingArea, field, child.getField(key));
				}
			}
		});
	}

	public void open() {
		handle.select();
		canvas.setBody(body);
	}

	public void close() {
		handle.deSelect();
		canvas.clearBody();
	}
	
	public void setCanvas(TabControlField canvas){
		this.canvas = canvas;
		this.canvas.addHandle(handle);
	}

	public void addFocusChangeObserver(FocusChangeListener observer) {
		observers.addElement(observer);
	}

	public String getLabel() {
		return label;
	}

	private void drawField(final Manager renderingArea, FormField field,String value) {
		Field detail = null;
		if (isNotEmpty(value)) {
			detail = new LabelField(drawKey(field), LabelField.FOCUSABLE) {
				protected void paint(Graphics graphics) {
					graphics.setColor(Color.GRAY);
					super.paint(graphics);
				}
			};
		} else {
			detail = BoldRichTextField.getSemiBoldRichTextField(drawKey(field),value);
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

	public void focusChanged(Field field, int eventType) {
		if (field instanceof TabHandleField) {
			notifyObservers(eventType);
		}
		
	}

	private void notifyObservers(int eventType) {
		for (int i = 0; i < observers.size(); i++) {
			((FocusChangeListener) observers.elementAt(i)).focusChanged(this, eventType);
		}
	}
}
