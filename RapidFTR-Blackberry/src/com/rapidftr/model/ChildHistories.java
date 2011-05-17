package com.rapidftr.model;

import java.util.Enumeration;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class ChildHistories {
	private final String histories;
	private JSONArray historiesJSON;

	public ChildHistories(String histories) {
		this.histories = histories;
		initializeJSON();
	}

	private void initializeJSON() {
		try {
			if (histories != null) {
				historiesJSON = new JSONArray(histories);
			} else {
				historiesJSON = new JSONArray();
			}
		} catch (JSONException e) {
			throw new RuntimeException("Invalid  History Format"+ e.getMessage());
		}
	}

	public void forEachHistory(HistoryAction action) {
		try {
			if (histories != null) {
				for (int i = 0; i < historiesJSON.length(); i++) {
					JSONObject history = historiesJSON.getJSONObject(i);
					forEachChangedField(action, history);
				}
			}
		} catch (JSONException e) {
			throw new RuntimeException("Invalid  History Format"+ e.getMessage());
		}
	}

	private void forEachChangedField(HistoryAction action, JSONObject history) throws JSONException {
		JSONObject changes = history.getJSONObject("changes");
		Enumeration changedFields = changes.keys();
		while (changedFields.hasMoreElements()) {
			String changedFieldName = (String) changedFields.nextElement();
			JSONObject changedFieldObject = changes.getJSONObject(changedFieldName);
			action.execute(new ChildHistoryItem(history.getString("user_name"), 
						   history.getString("datetime"),
						   changedFieldName,
						   changedFieldObject.getString("from"),
						   changedFieldObject.getString("to")
						   ));
		}
	}

	public boolean isNotEmpty(){
		return (null != histories) && historiesJSON.length()>0;
	}
}
