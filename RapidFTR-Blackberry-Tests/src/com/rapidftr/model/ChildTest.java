package com.rapidftr.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.rapidftr.form.FormField;
import com.rapidftr.form.FormFieldAction;
import com.rapidftr.form.Forms;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;

public class ChildTest {

	private Forms forms;

	@Before
	public void setUp() throws JSONException {
		forms = new Forms(new JSONArray("[{'fields':[{'name':'name','enabled':true,'type':'text_field','display_name':'Name'}]}]"));
	}

	@Test
	public void twoChildrenShouldBeSameIfHaveSameUniqueIdentifier() {
		Child alice = ChildFactory.newChild();
		alice.setField("name", "Alice");
		alice.setField("unique_identifier", "unique_identifier");

		Child joy = ChildFactory.newChild();
		joy.setField("name", "joy");
		joy.setField("unique_identifier", "unique_identifier");
		assertTrue(joy.equals(alice));
	}

	@Test
	public void twoChildrenShouldBeSameIfHaveSameCouchId() {
		Child alice = ChildFactory.newChild();
		String couchId = "someRandomCouchId";
		alice.setField("name", "Alice");
		alice.setField("_id", couchId);

		Child joy = ChildFactory.newChild();
		joy.setField("name", "joy");
		joy.setField("_id", couchId);
		assertTrue(joy.equals(alice));
	}

	@Test
	public void shouldCreateNewChildWithSupliedFormData() {
		forms.forEachField(new FormFieldAction() {
			@Override
			public void execute(FormField field) {
				field.setValue("someName");
			}
		});
		Child alice = ChildFactory.newChild();
		alice.update(forms);
		assertEquals("someName", alice.getField("name"));
	}

	@Test
	public void shouldUpdateChildWithSupliedFormData() {
		forms.forEachField(new FormFieldAction() {
			@Override
			public void execute(FormField field) {
				field.setValue("someName");
			}
		});
		Child alice = ChildFactory.newChild();
		String couchId = "someRandomCouchId";
		alice.setField("name", "Alice");
		alice.setField("_id", couchId);
		alice.update(forms);
		assertEquals("someName", alice.getField("name"));
	}

	@Test
	public void isNewChildShouldReturnTrueIfChildHaveNullUniqueIdentifier() {
		Child joy = ChildFactory.newChild();
		joy.setField("name", "joy");
		assertTrue(joy.isNewChild());
	}

	@Test
	public void isNewChildShouldReturnFalseIfChildHaveSomeUniqueIdentifier() {
		Child joy = ChildFactory.newChild();
		joy.setField("name", "joy");
		joy.setField("unique_identifier", "unique_identifier");
		assertFalse(joy.isNewChild());
	}

	@Test
	public void isNewChildShouldReturnFalseEvenIfThereIsASynchFailure() {
		Child child = ChildFactory.newChild();
		child.syncFailed("Some synch failure");
		assertTrue(child.isNewChild());
	}

	@Test
	public void shouldNotPutNullValuesInHashTable() {
		Child child = ChildFactory.newChild();
		child.setField("name", null);
	}

	@Test
	public void shouldSetCreatedAt() {
		Child child = ChildFactory.newChild();
		assertNotNull(child.getField(Child.CREATED_AT_KEY));
	}
	
	@Test
	public void shouldSetFlaggedKey() {
		Child child = ChildFactory.newChild();
		child.flagRecord("Reason");
		assertNotNull(child.getField(Child.FLAGGED_KEY));
		assertTrue("true".equals(child.getField(Child.FLAGGED_KEY)));
	}
	
	@Test
	public void isUpdatedshouldReturnTrueWhenRecordIsFlagged() {
		Child child = ChildFactory.newChild();
		child.flagRecord("Reason");
		assertTrue(child.isUpdated());
	}
	
	@Test
	public void childStatusshouldBeFlaggedWhenFlaggedRecordIsUpdated() {
		Child child = ChildFactory.newChild();
		child.flagRecord("Reason");
		assertTrue(child.isUpdated());
		child.setField("name", "name");
		assertEquals(ChildStatus.FLAGGED, child.childStatus());
	}

	@Test
	public void shouldGetPostData() {
		Child child = ChildFactory.newChild();
		PostData data = child.getPostData();
		Part[] parts = data.getParts();
		assertEquals(2, parts.length);
		assertEquals("form-data; name=\"child[_id]\"", parts[0].getHeaders()[0]
				.getValue());
		assertEquals("form-data; name=\"child[created_at]\"", parts[1]
				.getHeaders()[0].getValue());
	}

	@Test
	public void shouldGetPostDataWithoutHistories() {
		Child child = ChildFactory.newChild();
		child.setField("histories", "[histories]");
		PostData data = child.getPostData();
		Part[] parts = data.getParts();
		assertEquals(2, parts.length);
	}
}
