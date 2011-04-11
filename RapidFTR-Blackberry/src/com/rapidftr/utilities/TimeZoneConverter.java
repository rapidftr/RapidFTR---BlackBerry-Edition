package com.rapidftr.utilities;

import java.util.TimeZone;

public class TimeZoneConverter
{
    public long convertUTCto(TimeZone timeZoneToConvertTo, long timeToConvert) {
        int millisecondsFromUTC = timeZoneToConvertTo.getRawOffset();
        return timeToConvert + millisecondsFromUTC;
    }
}
