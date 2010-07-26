package com.rapidftr.model;

import java.util.Enumeration;
import java.util.Hashtable;

public class Child {

    private final String name;
    private final Hashtable fields = new Hashtable();

    public Child(String name) {
        this.name = name;
    }

    public String toString() {
        return "Child: " + name;
    }

    public String getName() {
        return name;
    }

    public String getField(String fieldName) {
        return (String) fields.get(fieldName);
    }

    public Enumeration getFieldNames() {
        return fields.keys();
    }

    public void setField(String fieldName, String fieldValue) {
        fields.put(fieldName, fieldValue);
    }


}

