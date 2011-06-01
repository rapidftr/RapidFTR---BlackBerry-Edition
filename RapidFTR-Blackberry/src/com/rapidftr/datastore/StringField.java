package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public class StringField extends Field {
	public StringField(String attribute) {
		super(attribute);
	}

	public int compare(Child child1, Child child2) {
		return stringIgnoreCaseComparator(child1.getField(attribute), child2
				.getField(attribute));
	}

	private int stringIgnoreCaseComparator(String firstString,
			String secondString) {
		int result = (firstString == null ? "" : firstString.toLowerCase())
				.compareTo((secondString == null ? "" : secondString
						.toLowerCase()));
		return result > 0 ? 1 : (result == 0 ? result : -1); // to avoid weird
		// emulator
		// behavior
	}

	public int hashCode() {
		return this.attribute.hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof StringField))
			return false;
		StringField field1 = (StringField) obj;
		if (field1.attribute == null)
			return false;

		return field1.attribute.equals(this.attribute);
	}
}