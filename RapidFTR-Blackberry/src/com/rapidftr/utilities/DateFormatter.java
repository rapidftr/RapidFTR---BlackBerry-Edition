package com.rapidftr.utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import net.rim.device.api.i18n.SimpleDateFormat;

public class DateFormatter {

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ssz";
    private TimeZone defaultTimeZone;
    private final IFormatDates defaultDateFormat;
    private final TimeZoneConverter timeZoneConverter = new TimeZoneConverter();

    public DateFormatter(TimeZone timeZone, IFormatDates format) {
        this.defaultTimeZone = timeZone;
        defaultDateFormat = format;
    }

    public String getCurrentFormattedDateTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new SimpleDateFormat(TIME_FORMAT).format(cal);
    }

    public String formatToDefaultTimeZone(long date) {
        long timeInDefaultTimeZone = timeZoneConverter.convertUTCto(defaultTimeZone, date);
        return format(timeInDefaultTimeZone);
    }

    public String format(long dateToConvert) {
        return defaultDateFormat.format(new Date(dateToConvert));
    }
}