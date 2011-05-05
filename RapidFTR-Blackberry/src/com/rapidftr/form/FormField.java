package com.rapidftr.form;

import java.util.Vector;

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
		try {
			JSONArray array = getOptionJSONArray();
			for (int i = 0; i < array.length(); i++) {
				optionAction.execute(array.getString(i));
			}
		} catch (JSONException e) {
		}
	}

	private JSONArray getOptionJSONArray() throws JSONException {
		return new JSONArray(getProperty("option_strings"));
	}

	public String[] getOptionsArray() {
		final Vector options = new Vector();
		forEachOption(new OptionAction() {
			public void execute(String option) {
				options.addElement(option);
			}
		});
		String[] optionsArray = new String[options.size()];
		options.copyInto(optionsArray);
		return optionsArray;
	}
}
