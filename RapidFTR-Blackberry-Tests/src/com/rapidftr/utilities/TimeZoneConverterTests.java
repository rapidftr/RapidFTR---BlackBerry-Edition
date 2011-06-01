package com.rapidftr.utilities;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class TimeZoneConverterTests
{
    private final long MILLISECONDS_PER_HOUR = 60 * 60 * 1000;

    @Test
    public void ShouldAddTimezoneOffsetToDate()
    {
        int hour = 1;
        long utcTime = new Date(2011,1,1,hour,30).getTime();

        TimeZoneConverter timeZoneConverter = new TimeZoneConverter();
        TimeZone easternStandardTime = TimeZone.getTimeZone("Australia/Brisbane");
        long convertedTime = timeZoneConverter.convertUTCto(easternStandardTime, utcTime);

        long expectedTime = utcTime + (10 * MILLISECONDS_PER_HOUR);

        assertEquals(expectedTime, convertedTime);
    }

    @Test
    public void ESTTimeZoneShouldReturnPlusTenHours()
    {
        TimeZone easternStandardTime = TimeZone.getTimeZone("Australia/Brisbane");
        long expectedTimeOffset = 10 * MILLISECONDS_PER_HOUR;

        assertEquals(expectedTimeOffset, easternStandardTime.getRawOffset());
    }
}
