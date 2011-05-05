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
		try {
			JSONArray fields = getFields();
			for (int i = 0; i < fields.length(); i++) {
				fieldAction.execute(new FormField(fields.getJSONObject(i)));
			}
		} catch (JSONException e) {
		}
	}

	private JSONArray getFields() throws JSONException {
		return new JSONArray(getProperty("fields"));
	}

	private boolean isEmpty() {
		try {
			return getFields().length() == 0;
		} catch (JSONException e) {
			return true;
		}
	}

	public String toString() {
		return getName();
	}

	public boolean isNotEmpty() {
		return !isEmpty();
	}

}
