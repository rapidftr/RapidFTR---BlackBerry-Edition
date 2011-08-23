package com.rapidftr.model;

import org.junit.Before;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.*;
import static org.junit.Assert.fail;

public class ChildHistoryChangeEntryTest {
	private ChildHistoryChangeEntry childHistoryChangeEntry;
	
	@Test
	public void shouldDescribeChangeCorrectly(){
		childHistoryChangeEntry = new ChildHistoryChangeEntry("name", "oldValue", "newValue");
		assertEquals("name changed from oldValue to newValue",childHistoryChangeEntry.getChangeDescription());
	}
	
	@Test
	public void shouldDescribeInitializationCorrectly(){
		childHistoryChangeEntry = new ChildHistoryChangeEntry("name", "", "newValue");
		assertEquals("name initialized to newValue",childHistoryChangeEntry.getChangeDescription());
	}
}

