package com.rapidftr.model;

import com.rapidftr.utilities.DateFormatter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
