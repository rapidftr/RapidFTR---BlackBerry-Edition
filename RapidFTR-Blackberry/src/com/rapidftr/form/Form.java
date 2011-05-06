package com.rapidftr.form;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Form extends JSONData {

	private Vector formFields = new Vector();

	public Form(JSONObject jsonObject) {
		super(jsonObject);
		try {
			JSONArray fields = new JSONArray(getProperty("fields"));
			for (int i = 0; i < fields.length(); i++) {
				formFields.addElement(new FormField(fields.getJSONObject(i)));
			}
		} catch (JSONException e) {
		}

	}

	public String getUniqueId() {
		return getProperty("unique_id");
	}

	public void forEachField(FormFieldAction fieldAction) {
		for (int i = 0; i < formFields.size(); i++) {
			FormField field = (FormField) formFields.elementAt(i);
			if (field.isEnabled()) {
				fieldAction.execute(field);
			}
		}
	}

	private boolean isEmpty() {
		return formFields.isEmpty();
	}

	public String toString() {
		return getName();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

}
