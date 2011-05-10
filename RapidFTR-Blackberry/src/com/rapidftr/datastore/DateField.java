package com.rapidftr.datastore;

import java.util.Date;

import net.rim.device.api.io.http.HttpDateParser;

import com.rapidftr.model.Child;

public class DateField extends Field {

	public DateField(String attribute, boolean isAscending) {
		super(attribute, isAscending);
	}

	public DateField(String attribute) {
		this(attribute, false);
	}

	public int compare(Child child, Child otherChild) {
		Date date = new Date(parse((String)child.getField(attribute)));
		Date otherDate = new Date(parse((String)otherChild.getField(attribute)));
		
		if (date.getTime() > otherDate.getTime()) {
			return 1;
		} else if (date.getTime() == otherDate.getTime()) {
			return 0;
		}
		return -1;
	}

	protected long parse(String date) {
		return HttpDateParser.parse(date);
	}
}
