package com.rapidftr.utilities;

import net.rim.device.api.i18n.SimpleDateFormat;

public class DefaultBlackBerryDateFormat implements IFormatDates {

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm z";
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT);

    public DefaultBlackBerryDateFormat() {
    }

    public String format(Object o) {
        return simpleDateFormat.format(o);
    }
}
