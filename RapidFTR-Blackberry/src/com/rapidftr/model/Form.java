package com.rapidftr.model;

import java.util.Vector;

import net.rim.device.api.util.Persistable;

public class Form implements Persistable {

	private String name;
	private final String id;
	private final Vector fieldList;

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
	
	

}
