package com.rapidftr.model;

import com.rapidftr.utilities.DateFormatter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChildFactory {

    private ChildFactory() {
    }

    public static Child newChild() {
        DateFormatter dateFormatterMock = mock(DateFormatter.class);
        when(dateFormatterMock.getCurrentFormattedDateTime()).thenReturn("2010-11-2 01:00:00GMT");
        return new Child(dateFormatterMock);
    }
}
