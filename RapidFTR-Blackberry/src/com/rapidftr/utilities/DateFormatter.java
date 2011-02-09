package com.rapidftr.utilities;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.util.Persistable;

import java.util.Calendar;
import java.util.TimeZone;

public class DateFormatter implements Persistable {

    public String getCurrentFormattedDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ssz").format(cal);
    }

}
