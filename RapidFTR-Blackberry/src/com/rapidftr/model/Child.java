package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;
import java.util.Vector;

import net.rim.device.api.util.Persistable;

import com.rapidftr.utilities.FileUtility;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.RandomStringGenerator;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;

public class Child implements Persistable {

	private final Hashtable data;

	public Child() {
		data = new Hashtable();
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

	public void createUniqueId(String userName) {
		String unknownLocation = "xxx";
		String blackBerryPrefix = "B*";
		String truncatedLocation = data.get("last_known_location") == null ? unknownLocation
				: data.get("last_known_location").toString().substring(0, 3)
						.toLowerCase();
		data.put("unique_identifier", blackBerryPrefix + userName
				+ truncatedLocation + RandomStringGenerator.generate(5));
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

}
