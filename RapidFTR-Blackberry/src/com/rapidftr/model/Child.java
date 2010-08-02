package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.util.Persistable;

public class Child implements Persistable {

	private final Hashtable data;

	public Child(Hashtable data) {
		this.data = data;
	}

	public String toString() {

		if (data == null)
			return null;

		StringBuffer buffer = new StringBuffer();

		for (Enumeration list = data.elements(); list.hasMoreElements();) {
			Hashtable formData = (Hashtable) list.nextElement();

			buffer.append("[");
				for (Enumeration keyList = formData.keys(); keyList
						.hasMoreElements();) {
					Object key = keyList.nextElement();
					Object value;
					if(key.equals(Form.FORM_NAME))
					{
						value = formData.get(key);
					}
					else
					{
						
						value = ((Object[]) formData.get(key))[1];
					}
					buffer.append(key + ":" + value + ",");

				}
				buffer.append("],");
		}

		return buffer.toString();
	}

}
