package com.rapidftr;

import static org.junit.Assert.*;

import org.junit.Test;


public class KeyTest {
	@Test
	public void getValueReturnsHashCode() throws Exception {
		assertEquals("key".hashCode(), new Key("key").getValue());
	}
}
