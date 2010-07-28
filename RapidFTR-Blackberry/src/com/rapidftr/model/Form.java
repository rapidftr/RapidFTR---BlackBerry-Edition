package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Vector;

import com.rapidftr.controllers.NewChildController;
import com.rapidftr.controls.BlankSeparatorField;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.util.Persistable;

public class Form  {

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
			return name.equals(form.name) && id.equals(form.id)
					&& getFieldList().equals(form.getFieldList());
		} catch (NullPointerException e) {
			return false;
		}
	}

	public String formatedSring() {

		return "[" + name + "," + id + "," + getFieldList().toString();
	}

	public String toString() {

		return name;
	}

	public Vector getFieldList() {
		return fieldList;
	}

	public void initializeLayout(NewChildController newChildController) {
		layoutManager = new VerticalFieldManager();
		for (Enumeration list = fieldList.elements(); list.hasMoreElements();) {
			FormField formField = (FormField) list.nextElement();
			formField.initializeLayout(newChildController);
			layoutManager.add(formField.getLayout());
			layoutManager.add(new BlankSeparatorField(10));
		}
	}

	public Manager getLayout() {
		return layoutManager;
	}

}
