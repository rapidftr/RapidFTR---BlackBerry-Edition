package com.rapidftr.model;

import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.screens.ManageChildScreen;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import java.util.Enumeration;
import java.util.Vector;

public class Form {

	public static final String FORM_NAME = "form_name";
	private String name;
	private final String id;
	private final Vector fieldList;
	private Manager layoutManager;

	public Form(String name, String id, Vector fieldList) {
		this.name = name;
		this.id = id;
		this.fieldList = fieldList;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (this == obj)
			return true;

		if (!(obj instanceof Form))
			return false;

		Form form = (Form) obj;

		try {
			return name.equals(form.name) && getId().equals(form.getId())
					&& getFieldList().equals(form.getFieldList());
		} catch (NullPointerException e) {
			return false;
		}
	}

	public String toString() {

		return name;
	}

	public Vector getFieldList() {
		return fieldList;
	}
	public void initializeLayout(ManageChildScreen newChildScreen) {
		FormField formField = null;
		Enumeration list = null;
		Object nextElement = null;
		try {
			layoutManager = new VerticalFieldManager();
			if (fieldList != null) {
				for (list = fieldList.elements(); list.hasMoreElements();) {
					nextElement = list.nextElement();
					if (nextElement != null) {
						formField = (FormField) nextElement;
						formField.initializeLayout(newChildScreen);
						layoutManager.add(formField.getLayout());
						layoutManager.add(new BlankSeparatorField(10));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void initializeLayoutWithChild(ManageChildScreen newChildScreen,
			Child child) {
		layoutManager = new VerticalFieldManager();
		for (Enumeration list = fieldList.elements(); list.hasMoreElements();) {
			Object nextElement = list.nextElement();
			if (nextElement != null) {
				FormField formField = (FormField) nextElement;
				formField.initializeLayout(newChildScreen);
				Object fieldValue = child.getField(formField.getName());
				formField.setValue((fieldValue != null) ? fieldValue.toString()
						: "");
				layoutManager.add(formField.getLayout());
				layoutManager.add(new BlankSeparatorField(10));
			}
		}
	}

	public boolean isEmpty() {
		for (Enumeration list = fieldList.elements(); list.hasMoreElements();) {
			Object nextElement = list.nextElement();
			if (nextElement != null) {
				FormField formField = (FormField) nextElement;
				if (!formField.isEmpty()) {
					return false;
				}
			}
		}
		return true;
	}

	public Manager getLayout() {
		return layoutManager;
	}

	public String getId() {
		return id;
	}

}
