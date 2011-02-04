package com.rapidftr.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static junit.framework.Assert.*;

public class ChildHistoryTest {
    private Child child;

    @Before
    public void setup() {
        String histories = "[{\"changes\":{\"date_of_separation\":{\"from\":\"\",\"to\":\"1-2 weeks ago\"}},\"datetime\":\"01/02/2011 22:01\",\"user_name\":\"rapidftr\"}]";
        child = new Child();
        child.setField("histories", histories);
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
}
