package com.rapidftr.model;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Test;

public class ChildTest {

	@Test
	public void shouldCreateUniqueIdOnEveryInvocationOfCreateUniqueIdMethod() {
		String userName = "rapidFTR";
		Child alice = new Child();
		alice.setField("name", "Alice");
		alice.setField("last_known_location", "Leh");
		alice.createUniqueId(userName);
		String firstId = alice.getField("unique_identifier").toString();
		alice.createUniqueId(userName);
		assertNotSame(firstId, alice.getField("unique_identifier").toString());
	}

	@Test
	public void twoChildrenShouldBeSameIfHaveSameUniqueIdentifier() {
		Child alice = new Child();
		alice.setField("name", "Alice");
		alice.setField("unique_identifier", "unique_identifier");

		Child joy = new Child();
		joy.setField("name", "joy"); 
		joy.setField("unique_identifier", "unique_identifier");
		assertTrue(joy.equals(alice));
	}

	@Test
	public void twoChildrenShouldBeSameIfHaveSameCouchId() {
		Child alice = new Child();
		String couchId = "someRandomCouchId";
		alice.setField("name", "Alice");
		alice.setField("_id", couchId);

		Child joy = new Child();
		joy.setField("name", "joy");
		joy.setField("_id", couchId);
		assertTrue(joy.equals(alice));
	}


}
