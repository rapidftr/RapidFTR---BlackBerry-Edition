package com.rapidftr.model;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Vector;

import org.json.me.JSONException;
import org.junit.Before;
import org.junit.Test;

public class ChildHistoriesTest {
	
	private Child child;
	
	@Before
    public void setup() {
        String histories = "[{\"changes\":{\"date_of_separation\":{\"from\":\"\",\"to\":\"1-2 weeks ago\"}},\"datetime\":\"01/02/2011 22:01\",\"user_name\":\"rapidftr\"}]";
        child = ChildFactory.newChild();
        child.setField("histories", histories);
    }


	@Test
	public void forEachHistoryItemIgonresNullHistories() throws Exception {
		ChildHistories childHistories = new ChildHistories(null);
		childHistories.forEachHistory(new HistoryAction() {
			@Override
			public void execute(ChildHistoryItem historyItem) {
				fail("should be ignored");
			}
		});
		assertFalse(childHistories.isNotEmpty());
	}
	
	@Test(expected = RuntimeException.class)
	public void forEachThrowsExceptionOnInvalidHistories() throws Exception {
		String histories = "{[";
		ChildHistories childHistories = new ChildHistories(histories);
		childHistories.forEachHistory(new HistoryAction() {
			@Override
			public void execute(ChildHistoryItem historyItem) {
				fail("should throw Exception");
			}
		});
	}
	
	@Test
	public void isNotEmpty() throws Exception {
		
	}
	
	@Test
    public void shouldParseHistories() {
        final ChildHistories historyItems = child.getHistory();
        assertTrue(historyItems.isNotEmpty());
        historyItems.forEachHistory(new HistoryAction() {
			@Override
			public void execute(ChildHistoryItem historyItem) {
				assertEquals("01/02/2011 22:01", historyItem.getChangeDateTime());
				assertEquals(new ChildHistoryChangeEntry("date_of_separation","","1-2 weeks ago")
						, ((ChildHistoryChangeEntry) historyItem.getData().elementAt(0)));
				assertEquals("rapidftr", historyItem.getUsername());
			}
		});
    }

    @Test
    public void shouldReturnFieldChangeDescriptionWithGroupedChanges() {
    	String histories = "[{\"changes\":{\"date_of_separation\":{\"from\":\"\",\"to\":\"1-2 weeks ago\"},\"age\":{\"from\":\"2\",\"to\":\"5\"}},\"datetime\":\"01/02/2011 22:01\",\"user_name\":\"rapidftr\"}]";
    	child.setField("histories", histories);
        final ChildHistories historyItems = child.getHistory();
        assertTrue(historyItems.isNotEmpty());
        historyItems.forEachHistory(new HistoryAction() {
			@Override
			public void execute(ChildHistoryItem historyItem) {
				assertEquals("  date_of_separation initialized to 1-2 weeks ago\n  age changed from 2 to 5\nBy rapidftr", 
		                historyItem.getFieldChangesDescription());
		        assertEquals("01/02/2011 22:01  date_of_separation initialized to 1-2 weeks ago\n  age changed from 2 to 5\nBy rapidftr",
		                historyItem.toString());
			}
		});
        
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
