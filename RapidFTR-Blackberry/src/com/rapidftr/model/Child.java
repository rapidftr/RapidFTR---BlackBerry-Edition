package com.rapidftr.model;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.util.Persistable;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.rapidftr.utilities.FileUtility;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.RandomStringGenerator;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;

public class Child implements Persistable {

	private final Hashtable data;

	public Child() {
		data = new Hashtable();
		data.put("_id", RandomStringGenerator.generate(32));
	}

	public String toFormatedString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (Enumeration keyList = data.keys(); keyList.hasMoreElements();) {
			Object key = keyList.nextElement();
			Object value = data.get(key);
			buffer.append(key + ":" + value + ",");
		}
		buffer.append("]");
		return buffer.toString();
	}

	public String toString() {
		return (String) data.get("name");
	}

	public Part[] getPostData() {

		Vector parts = new Vector();

		for (Enumeration keyList = data.keys(); keyList.hasMoreElements();) {
			Object key = keyList.nextElement();
			Object value = data.get(key);

			if (key.equals("current_photo_key")) {
				Arg[] headers = new Arg[2];
				headers[0] = new Arg("Content-Disposition",
						"form-data; name=\"child[" + "photo" + "]\"");
				headers[1] = HttpUtility.HEADER_CONTENT_TYPE_IMAGE;
				byte[] imageData = FileUtility.getByteArray(value.toString());
				Part part = new Part(imageData, headers);
				parts.addElement(part);
				continue;
			}

			Arg[] headers = new Arg[1];
			headers[0] = new Arg("Content-Disposition",
					"form-data; name=\"child[" + key + "]\"");

			Part part = new Part(value.toString().getBytes(), headers);

			parts.addElement(part);

		}

		Part[] anArray = new Part[parts.size()];
		parts.copyInto(anArray);
		return anArray;
	}

	public void setField(String name, Object value) {
		data.put(name, value);
	}

	public Object getField(String key) {
		return data.get(key);
	}

	public Hashtable getKeyMap() {
		return data;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((data == null) ? 0 : data.get("_id").hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Child other = (Child) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else {
			if (data.get("_id") != null && other.data.get("_id") != null
					&& data.get("_id").equals((other.data.get("_id"))))
				return true;
			if (data.get("unique_identifier") != null
					&& other.data.get("unique_identifier") != null
					&& data.get("unique_identifier").equals(
							(other.data.get("unique_identifier"))))
				return true;
		}

		return false;
	}

	public void updateField(String name) {
		if (!data.containsKey(name)) {
			data.put(name, "");
		}

	}

	public static Child create(Vector forms) {
		Child child = new Child();
		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			Form form = (Form) list.nextElement();
			for (Enumeration fields = form.getFieldList().elements(); fields
					.hasMoreElements();) {
				FormField field = (FormField) fields.nextElement();
				child.setField(field.getName(), field.getValue());
			}
		}
		return child;
	}

	public void update(String userName, Vector forms) {
		try {
			JSONArray histories = getField("histories") != null ? new JSONArray(
					getField("histories").toString())
					: new JSONArray();
			boolean isSomeFieldchanged = false;
			JSONObject history = new JSONObject();
			history.put("datetime", new Date());
			history.put("user_name", userName);
			JSONObject historyItems = new JSONObject();
			for (Enumeration list = forms.elements(); list.hasMoreElements();) {
				Form form = (Form) list.nextElement();
				for (Enumeration fields = form.getFieldList().elements(); fields
						.hasMoreElements();) {
					FormField field = (FormField) fields.nextElement();
					Object previousValue = getField(field.getName().toString());
					if (previousValue != null
							&& !previousValue.equals(field.getValue())) {
						isSomeFieldchanged = true;
						setField(field.getName(), field.getValue());
						JSONObject historyItem = new JSONObject();
						historyItem.put("from", previousValue.toString());
						historyItem.put("to", field.getValue().toString());
						historyItems.put(field.getName(), historyItem);
					}
				}
			}

			if (isSomeFieldchanged) {
				history.put("changes", historyItems);
				histories.put(history);
			}
			setField("histories", histories.toString());
		} catch (JSONException e) {
			throw new RuntimeException("Invalid  History Format"
					+ e.getMessage());
		}
	}

	public Vector getHistory() {
		Vector historyLogs = new Vector();
		try {
			JSONArray histories = new JSONArray(getField("histories")
					.toString());
			for (int i = 0; i < histories.length(); i++) {
				JSONObject history = null;
				history = histories.getJSONObject(i);
				JSONObject changes = history.getJSONObject("changes");
				Enumeration changedFields = changes.keys();
				while (changedFields.hasMoreElements()) {
					String changedFieldName = (String) changedFields
							.nextElement();
					JSONObject changedFieldObject = changes
							.getJSONObject(changedFieldName);
					String changeDateTime = history.getString("datetime");
					String oldValue = changedFieldObject.getString("from");
					String newalue = changedFieldObject.getString("to");
					if (oldValue.equals("")) {
						historyLogs.addElement(changeDateTime + " "
								+ changedFieldName + " intialized to "
								+ newalue + " By "
								+ history.getString("user_name"));
					} else {
						historyLogs.addElement(changeDateTime + " "
								+ changedFieldName + " changed from "
								+ oldValue + " to " + newalue + " By "
								+ history.getString("user_name"));

					}
				}

			}
		} catch (JSONException e) {
			throw new RuntimeException("Invalid  History Format"
					+ e.getMessage());
		}

		return historyLogs;
	}
}
