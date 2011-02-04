package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public class StringField extends Field {
	public StringField(String attribute) {
		super(attribute);
	}

	public int compare(Child child1, Child child2) {
		return stringIgnoreCaseComparator((String) child1.getField(attribute),
				(String) child2.getField(attribute));
	}

	private int stringIgnoreCaseComparator(String firstString,
			String secondString) {
		int result = (firstString == null ? "" : firstString.toLowerCase())
						.compareTo((secondString == null ? "" : secondString
								.toLowerCase()));
		return result > 0 ? 1 : (result == 0 ? result : -1); // to avoid weird emulator behavior
	}
}
