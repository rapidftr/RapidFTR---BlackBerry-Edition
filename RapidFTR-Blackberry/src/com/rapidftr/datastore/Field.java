package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public abstract class Field {

	protected final String attribute;

	public Field(String attribute) {
		this(attribute, true);
	}

	public Field(String attribute, boolean isAscending) {
		this.attribute = attribute;
	}

	public abstract int compare(Child child1, Child child2);

}