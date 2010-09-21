package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.util.Persistable;

import com.rapidftr.services.FormService;
import com.rapidftr.utilities.FileUtility;
import com.rapidftr.utilities.HttpUtility;
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
				parts.addElement(multiPart(value, "photo",
						HttpUtility.HEADER_CONTENT_TYPE_IMAGE));
				continue;
			}
			if (key.equals("recorded_audio")) {
				if (value != null) {
					parts.addElement(multiPart(value, "audio",
							HttpUtility.HEADER_CONTENT_TYPE_AUDIO));
				}
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

	private Part multiPart(Object value, String paramName, Arg headerContentType) {
		Arg[] headers = new Arg[2];
		headers[0] = new Arg("Content-Disposition", "form-data; name=\"child["
				+ paramName + "]\"");
		headers[1] = headerContentType;
		byte[] imageData = FileUtility.getByteArray(value.toString());
		return new Part(imageData, headers);
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

}
