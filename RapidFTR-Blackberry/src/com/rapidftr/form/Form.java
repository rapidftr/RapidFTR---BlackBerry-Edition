package com.rapidftr.form;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Form extends JSONData {

	public Form(JSONObject jsonObject) {
		super(jsonObject);
	}

	public String getUniqueId() {
		return getProperty("unique_id");
	}

	public void forEachField(FormFieldAction fieldAction) {
		JSONArray fields = getFields();
		for (int i = 0; i < fields.length(); i++) {
			try {
				fieldAction.execute(new FormField(fields.getJSONObject(i)));
			} catch (JSONException e) {
			}
		}
	}

	private JSONArray getFields() {
		try {
			return new JSONArray(getProperty("fields"));
		} catch (JSONException e) {
			return new JSONArray();
		}
	}

	public boolean isEmpty() {
		return getFields().length() == 0;
	}
	
	public String toString() {
		return getName();
	}

}
