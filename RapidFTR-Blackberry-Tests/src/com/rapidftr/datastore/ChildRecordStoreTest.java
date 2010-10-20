package com.rapidftr.datastore;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.rapidftr.model.Child;

public class ChildRecordStoreTest {

	private ChildrenRecordStore childStore;

	@Before
	public void setup() {
		childStore = new ChildrenRecordStore(new TestStore("key"));
	}

	@Test
	public void saveShouldAppendNewChildAndSaveInStore() {
		int count = childStore.getAll().count();
		childStore.addOrUpdate(new Child());
		childStore.addOrUpdate(new Child());
		Assert.assertEquals(count + 2, childStore.getAll().count());
	}

	@Test
	public void saveShouldUpdateTheChildIfItExists() {
		Child childOne = new Child();
		childOne.setField("name", "NewChild");
		childOne.setField("_id", "1");
		childStore.addOrUpdate(childOne);
		int initial = childStore.getAll().count();

		Child updatedChildOne = new Child();
		updatedChildOne.setField("name", "UpdatedChild");
		updatedChildOne.setField("_id", "1");
		childStore.addOrUpdate(updatedChildOne);

		Assert.assertEquals(initial, childStore.getAll().count());
	}

	@Test
	public void ifPersistentStoreEmptySearchShouldResultEmptyArray() {
		Assert.assertEquals(childStore.search("Tom").length, 0);
	}
	
	@Test
	public void ifChildIsNotPresentInPersistentStoreSearchShouldReturnEmptyResults() {
		Assert.assertEquals(childStore.search("Harry").length, 0);
	}
	
	@Test
	public void ifChildWithNamePresentInPersistentStoreSearchShouldReturnTheChild() {
		Child Tom = new Child();
		String childName = "Tom";
		Tom.setField("name", childName);
		childStore.addOrUpdate(Tom);
		
		Child[] searchResults = childStore.search(childName);

		Assert.assertEquals(searchResults.length, 1);
		Assert.assertEquals(searchResults[0], Tom);
	}
	
	@Test
	public void searchShouldReturnAllTheChildrenWithSearchedName() {
		String childName = "Tom";
		Child child = new Child();
		child.setField("name", childName);
		childStore.addOrUpdate(child);
		
		Child child2 = new Child();
		child2.setField("name", childName);
		childStore.addOrUpdate(child2);
		
		Child[] searchResults = childStore.search(childName);

		Assert.assertEquals(searchResults.length, 2);
		Assert.assertEquals(searchResults[0], child);
		Assert.assertEquals(searchResults[1], child2);
	}
	
	@Test
	public void ifChildWithIdPresentInPersistentStoreSearchShouldReturnTheChild() {
		Child child = new Child();
		child.setField("name", "Tom");
		String childUID = "1";
		child.setField("unique_identifier", childUID);
		childStore.addOrUpdate(child);
		
		Child[] searchResults = childStore.search(childUID);

		Assert.assertEquals(searchResults.length, 1);
		Assert.assertEquals(searchResults[0], child);
	}

	@Test
	public void ifNameofOneChildAndIdOfOtherAreEqualSearchShouldReturnBothTheChildren() {
		String childNameAndUID = "1";
		
		Child child = new Child();
		child.setField("name", childNameAndUID);
		childStore.addOrUpdate(child);

		Child child2 = new Child();
		child2.setField("unique_identifier", childNameAndUID);
		childStore.addOrUpdate(child2);

		Child[] searchResults = childStore.search(childNameAndUID);

		Assert.assertEquals(searchResults.length, 2);
		Assert.assertEquals(searchResults[0], child);
		Assert.assertEquals(searchResults[1], child2);
	}
	
}
