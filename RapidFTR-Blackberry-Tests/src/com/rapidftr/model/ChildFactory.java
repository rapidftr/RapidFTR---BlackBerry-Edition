package com.rapidftr.model;

import net.rim.device.api.io.http.HttpDateParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

            protected String toLocalTime(String dateTime, TimeZone tz) {
                Calendar cal = Calendar.getInstance();
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz").parse(dateTime);
                    cal.setTime(date);
                } catch (ParseException e) {
                    return dateTime;
                }
                cal.setTimeZone(tz);
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
            }
        };
    }
}
