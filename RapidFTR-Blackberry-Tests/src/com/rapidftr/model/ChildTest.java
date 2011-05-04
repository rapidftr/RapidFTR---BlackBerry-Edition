package com.rapidftr.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Vector;

import com.rapidftr.form.Forms;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;

import org.json.me.JSONArray;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ChildTest {

	private Forms forms;

	@Before
	public void setUp() {
		forms = new Forms(new JSONArray());
		Vector<FormField> fieldList = new Vector<FormField>();
    	FormField nameTextField = mock(FormField.class);
    	nameTextField.name="name";
		when(nameTextField.getValue()).thenReturn("someName");
		when(nameTextField.getName()).thenReturn("name");
		fieldList.add(nameTextField);
		Form form = new Form("Basic_details", "basic_details", fieldList);
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

	@Ignore("Fix this test")
	public void shouldCreateNewChildWithSupliedFormData() {
		Child alice = ChildFactory.newChild();
        alice.update(forms);
		assertEquals("someName", alice.getField("name"));		
	}
	
	@Ignore("Fix this test")
	public void shouldUpdateChildWithSupliedFormData() {
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
    public void isNewChildShouldReturnFalseEvenIfThereIsASynchFailure(){
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
    public void shouldGetPostData() {
        Child child = ChildFactory.newChild();
        PostData data = child.getPostData();
        Part[] parts = data.getParts();
        // includes created_at, _id
        assertEquals(2, parts.length);
        assertEquals("form-data; name=\"child[_id]\"", parts[0].getHeaders()[0].getValue());
        assertEquals("form-data; name=\"child[created_at]\"", parts[1].getHeaders()[0].getValue());
    }
    
    @Test
    public void shouldGetPostDataWithoutHistories() {
        Child child = ChildFactory.newChild();
        child.setHistories("[histories]");
        PostData data = child.getPostData();
        Part[] parts = data.getParts();
        assertEquals(2, parts.length);
    }
}
