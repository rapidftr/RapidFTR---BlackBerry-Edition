package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.rapidftr.controls.BlankSeparatorField;
import com.rapidftr.screens.NewChildScreen;

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

	public void initializeLayout(NewChildScreen newChildScreen) {
		layoutManager = new VerticalFieldManager();
		for (Enumeration list = fieldList.elements(); list.hasMoreElements();) {
			FormField formField = (FormField) list.nextElement();
			formField.initializeLayout(newChildScreen);
			layoutManager.add(formField.getLayout());
			layoutManager.add(new BlankSeparatorField(10));
		}
	}

	public Manager getLayout() {
		return layoutManager;
	}

	public String getId() {
		return id;
	}

	public Hashtable getDetails() {
		
		Hashtable data = new Hashtable();
		
		data.put(Form.FORM_NAME,name);
		for (Enumeration list = fieldList.elements(); list.hasMoreElements();) {
			FormField field = (FormField) list.nextElement();
			data.put(field.name,field.getValue());
		}
		
		return data;
	}
	
	

}
