package com.rapidftr.model;

public class ChildFactory {

    private ChildFactory() {
    }

    public static Child newChild() {
        return new Child("2010-11-2 01:00:00GMT");
    }

    public static Child existingChild(String id) {
        Child child = new Child("2010-11-2 01:00:00GMT");
        child.setUniqueIdentifier("uniqueid");
        child.setId(id);
        return child;
    }
}
