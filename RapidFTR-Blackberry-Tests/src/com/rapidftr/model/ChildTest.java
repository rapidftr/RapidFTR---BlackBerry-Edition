package com.rapidftr.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class ChildTest {

	private Vector<Form> forms;

	@Before
	public void setUp() {
		forms = new Vector<Form>();
		Vector<FormField> fieldList = new Vector<FormField>();
    	FormField nameTextField = mock(FormField.class);
    	nameTextField.name="name";
		when(nameTextField.getValue()).thenReturn("someName");
		when(nameTextField.getName()).thenReturn("name");
		fieldList.add(nameTextField);
		Form form = new Form("Basic_details", "basic_details", fieldList);
		forms.add(form);
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

	@Test
	public void shouldCreateNewChildWithSupliedFormData() {
		Child alice = Child.create(forms);
		assertNotNull(alice);		
	}
	
	@Test
	public void shouldUpdateChildWithSupliedFormData() {
		Child alice = new Child();
		String couchId = "someRandomCouchId";
		alice.setField("name", "Alice");
		alice.setField("_id", couchId);		
		alice.update("rapidftr", forms);		
		assertEquals("someName", alice.getField("name"));
	}
	
	@Test
	public void isNewChildShouldReturnTrueIfChildHaveNullUniqueIdentifier() {
		Child joy = new Child();
		joy.setField("name", "joy");
		assertTrue(joy.isNewChild());
	}
	
	@Test
	public void isNewChildShouldReturnFalseIfChildHaveSomeUniqueIdentifier() {
		Child joy = new Child();
		joy.setField("name", "joy");
		joy.setField("unique_identifier", "unique_identifier");
		assertFalse(joy.isNewChild());
	}

    @Test
    public void isNewChildShouldReturnFalseEvenIfThereIsASynchFailure(){
        Child child = new Child();
        child.syncFailed("Some synch failure");
        assertTrue(child.isNewChild());
    }

	@Test
	public void shouldNotPutNullValuesInHashTable() {
		Child child = new Child();
		child.setField("name", null);
	}
}
