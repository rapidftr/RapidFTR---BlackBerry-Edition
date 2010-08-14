package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import net.rim.device.api.util.Persistable;
import com.rapidftr.utilities.FileUtility;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;

public class Child implements Persistable {

	private final Hashtable data;
	private String id;  // created and sent by server

	public Child(Hashtable data) {
		this.data = data;
	}

	public String toString() {

		if (data == null)
			return null;

		StringBuffer buffer = new StringBuffer();

		buffer.append("id:" + id);

		for (Enumeration list = data.elements(); list.hasMoreElements();) {
			Hashtable formData = (Hashtable) list.nextElement();

			buffer.append("[");

			for (Enumeration keyList = formData.keys(); keyList
					.hasMoreElements();) {
				Object keys = keyList.nextElement();
				Object value, key = null;
				if (keys.equals(Form.FORM_NAME)) {
					value = formData.get(keys);
				} else {

					value = ((Object[]) formData.get(keys))[1];
					key = ((Object[]) formData.get(keys))[0];
				}
				buffer.append(keys + ":" + "[" + key + "," + value + "],");

			}
			buffer.append("],");
		}

		return buffer.toString();
	}

	public Part[] getPostData() {

		Vector parts = new Vector();

		for (Enumeration list = data.elements(); list.hasMoreElements();) {
			Hashtable formData = (Hashtable) list.nextElement();

			for (Enumeration keyList = formData.keys(); keyList
					.hasMoreElements();) {
				Object key = keyList.nextElement();
				Object value;
				if (key.equals(Form.FORM_NAME)) {
					continue;
				}

				String type = (String) ((Object[]) formData.get(key))[0];
				value = ((Object[]) formData.get(key))[1];
				if (type.equals(PhotoUploadFormField.TYPE)) {
					Arg[] headers = new Arg[2];
					headers[0] = new Arg("Content-Disposition",
							"form-data; name=\"child[" + "photo" + "]\"");
					
					headers[1] = HttpUtility.HEADER_CONTENT_TYPE_IMAGE;

					byte[] imageData = FileUtility.getByteArray(value
							.toString());

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

		}

		Part[] anArray = new Part[parts.size()];
		parts.copyInto(anArray);
		return anArray;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

}
