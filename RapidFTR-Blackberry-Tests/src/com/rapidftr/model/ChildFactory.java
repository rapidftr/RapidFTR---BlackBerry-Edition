package com.rapidftr.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class ChildFactory {

    private ChildFactory() {
    }

    public static Child newChild() {
        return new Child() {
            protected String getCurrentFormattedDateTime() {
                Calendar cal = Calendar.getInstance();
                cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz").format(cal.getTime());
            }
        };
    }
}
