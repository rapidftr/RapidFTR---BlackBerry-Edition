package com.rapidftr.form;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class FormField extends JSONData {

	public FormField(JSONObject jsonObject) {
		super(jsonObject);
	}

	public String getType() {
		return getProperty("type");
	}

	public String getDisplayName() {
		return getProperty("display_name");
	}

	public void forEachOption(OptionAction optionAction) {
		JSONArray array = getOptionJSONArray();
		for (int i = 0; i < array.length(); i++) {
			try {
				optionAction.execute(array.getString(i));
			} catch (JSONException e) {
			}
		}
	}

	private JSONArray getOptionJSONArray() {
		try {
			return new JSONArray(getProperty("option_strings"));
		} catch (JSONException e) {
			return new JSONArray();
		}
	}


	public String[] getOptionsArray() {
		final String[] optionArray = new String[getOptionJSONArray().length()];
		forEachOption(new OptionAction() {
			int i = 0;
			public void execute(String option) {
				optionArray[i++] = option;
			}
		});
		return optionArray;
	}
}
