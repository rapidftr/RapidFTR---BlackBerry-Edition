package com.rapidftr.form;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class JSONData {

	private final JSONObject jsonObject;

	protected JSONData(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getName() {
		return getProperty("name");
	}

	public boolean isEnabled() {
		if("false".equals(getProperty("enabled"))){
			return false;
		}else{
			return true;
		}
	}

	protected String getProperty(String key) {
		try {
			return jsonObject.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

}