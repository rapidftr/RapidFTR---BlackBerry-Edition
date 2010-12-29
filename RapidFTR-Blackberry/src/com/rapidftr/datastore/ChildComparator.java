package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public class ChildComparator {

	private String[] attributes;

	public ChildComparator() {
		this(new String[] { "name" });
	}

	public ChildComparator(String[] attributes) {
		setAttributes(attributes);
	}
	
	public void setAttributes(String[] attributes) {
			this.attributes = attributes;
	}

	public int compare(Child child1, Child child2) {
		int attributeComparator = stringIgnoreCaseComparator((String) child1
				.getField(attributes[0]), (String) child2
				.getField(attributes[0]));

		for (int i = 1; i < attributes.length && attributeComparator==0; i++) {
				attributeComparator = stringIgnoreCaseComparator(
						(String) child1.getField(attributes[i]),
						(String) child2.getField(attributes[i]));
		}

		return attributeComparator;
	}

	private int stringIgnoreCaseComparator(String firstString,
			String secondString) {		
		return (firstString == null ?"" :firstString
				.toLowerCase()).compareTo((secondString == null ?""
				:secondString.toLowerCase()));
	}
}
