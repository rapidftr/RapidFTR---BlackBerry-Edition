package com.rapidftr.model;

import org.junit.Before;
import org.junit.Test;

import java.util.TimeZone;
import java.util.Vector;

import static junit.framework.Assert.*;

public class ChildHistoryTest {
    private Child child;

    @Before
    public void setup() {
        String histories = "[{\"changes\":{\"date_of_separation\":{\"from\":\"\",\"to\":\"1-2 weeks ago\"}},\"datetime\":\"01/02/2011 22:01\",\"user_name\":\"rapidftr\"}]";
        child = ChildFactory.newChild();
        child.setHistories(histories);
    }

    @Test
    public void shouldParseHistories() {
        final Vector historyItems = child.getHistory();
        assertEquals(1, historyItems.size());
        assertEquals("01/02/2011 22:01 date_of_separation intialized to 1-2 weeks ago By rapidftr", historyItems.get(0).toString());
    }

    @Test
    public void shouldReturnFalseIfNoChangesByOtherUsers() {
        assertFalse(child.hasChangesByOtherThan("rapidftr"));
    }

    @Test
    public void shouldReturnTrueIfChangesByOtherUsers() {
        assertTrue(child.hasChangesByOtherThan("foobar"));
    }

    @Test
    public void shouldDisplayTimeInLocalTimeZone() {
        String histories = "[{\"changes\":{\"date_of_separation\":{\"from\":\"\",\"to\":\"1-2 weeks ago\"}},\"datetime\":\"2011-02-08 14:00:00UTC\",\"user_name\":\"rapidftr\"}]";
        child.setHistories(histories);
        final Vector historyItems = child.getHistory(TimeZone.getTimeZone("EST"));
        assertEquals(1, historyItems.size());
        assertEquals("2011-02-08 09:00:00 date_of_separation intialized to 1-2 weeks ago By rapidftr", historyItems.get(0).toString());
    }

    @Test
    public void shouldReturnDateTimeAsIsWhenItIsNotParseable() {
        String histories = "[{\"changes\":{\"date_of_separation\":{\"from\":\"\",\"to\":\"1-2 weeks ago\"}},\"datetime\":\"unparseable\",\"user_name\":\"rapidftr\"}]";
        child.setHistories(histories);
        final Vector historyItems = child.getHistory(TimeZone.getTimeZone("EST"));
        assertEquals(1, historyItems.size());
        assertEquals("unparseable date_of_separation intialized to 1-2 weeks ago By rapidftr", historyItems.get(0).toString());
    }
}
