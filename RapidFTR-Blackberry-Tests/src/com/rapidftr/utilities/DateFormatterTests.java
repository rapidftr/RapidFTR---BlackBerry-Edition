package com.rapidftr.utilities;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;

public class DateFormatterTests
{
    @Test
    public void ShouldFormatDateToGivenTimeZone()
    {
        String expectedDateFormatResult = "20110101 09:00am";
        long dateToConvert = Date.UTC(2011,1,1,9,0,0);

        IFormatDates mockFormat = mock(IFormatDates.class);
        when(mockFormat.format(anyLong())).thenReturn(expectedDateFormatResult);

        TimeZone pacificTimezone = TimeZone.getTimeZone("PST");
        long expectedTime = dateToConvert + pacificTimezone.getRawOffset();
        DateFormatter dateFormatter = new DateFormatter(pacificTimezone, mockFormat);

        String formattedDate = dateFormatter.formatToDefaultTimeZone(dateToConvert);

        verify(mockFormat).format(new Date(expectedTime));
        assertEquals(formattedDate, expectedDateFormatResult);
    }

    @Test
    public void ShouldFormatDateAsProvided()
    {
        String expectedDateFormatResult = "20110101 09:00am";
        long dateToConvert = Date.UTC(2011,1,1,9,0,0);

        IFormatDates mockFormat = mock(IFormatDates.class);
        when(mockFormat.format(anyLong())).thenReturn(expectedDateFormatResult);

        DateFormatter dateFormatter = new DateFormatter(TimeZone.getTimeZone("PST"), mockFormat);

        String formattedDate = dateFormatter.format(dateToConvert);

        verify(mockFormat).format(new Date(dateToConvert));
        assertEquals(formattedDate, expectedDateFormatResult);
    }
}

