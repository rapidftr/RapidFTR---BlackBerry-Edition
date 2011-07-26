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


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (attribute != null ? !attribute.equals(field.attribute) : field.attribute != null) return false;

        return true;
    }

    public int hashCode() {
        return attribute != null ? attribute.hashCode() : 0;
    }
}