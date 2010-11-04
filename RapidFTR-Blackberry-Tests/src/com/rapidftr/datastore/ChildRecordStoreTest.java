package com.rapidftr.datastore;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Vector;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.rapidftr.model.Child;

public class ChildRecordStoreTest {

	private ChildrenRecordStore childrenStore;

	@Before
	public void setup() {
		childrenStore = new ChildrenRecordStore(new MockStore("key"));
	}

	@Test
	public void saveShouldAppendNewChildAndSaveInStore() {
		int count = childrenStore.getAll().count();
		childrenStore.addOrUpdate(new Child());
		childrenStore.addOrUpdate(new Child());
		Assert.assertEquals(count + 2, childrenStore.getAll().count());
	}

	@Test
	public void saveShouldUpdateTheChildIfItExists() {
		Child childOne = new Child();
		childOne.setField("name", "NewChild");
		childOne.setField("_id", "1");
		childrenStore.addOrUpdate(childOne);
		int initial = childrenStore.getAll().count();

		Child updatedChildOne = new Child();
		updatedChildOne.setField("name", "UpdatedChild");
		updatedChildOne.setField("_id", "1");
		childrenStore.addOrUpdate(updatedChildOne);

		Assert.assertEquals(initial, childrenStore.getAll().count());
	}

	@Test
	public void ifPersistentStoreIsEmptySearchShouldResultAnEmptyArray() {
		Assert.assertEquals(childrenStore.search("Tom").length, 0);
	}
	
	@Test
	public void ifChildIsNotPresentInPersistentStoreSearchShouldReturnEmptyResults() {
		Assert.assertEquals(childrenStore.search("Harry").length, 0);
	}
	
	@Test
	public void ifChildWithNamePresentInPersistentStoreSearchShouldReturnTheChild() {
		Child Tom = new Child();
		String childName = "Tom";
		Tom.setField("name", childName);
		childrenStore.addOrUpdate(Tom);
		
		Child[] searchResults = childrenStore.search(childName);

		Assert.assertEquals(searchResults.length, 1);
		Assert.assertEquals(searchResults[0], Tom);
	}
	
	@Test
	public void searchShouldReturnAllTheChildrenWithSearchedName() {
		String childName = "Tom";
		Child child = new Child();
		child.setField("name", childName);
		childrenStore.addOrUpdate(child);
		
		Child child2 = new Child();
		child2.setField("name", childName);
		childrenStore.addOrUpdate(child2);
		
		Child[] searchResults = childrenStore.search(childName);

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
		childrenStore.addOrUpdate(child);
		
		Child[] searchResults = childrenStore.search(childUID);

		Assert.assertEquals(searchResults.length, 1);
		Assert.assertEquals(searchResults[0], child);
	}

	@Test
	public void ifNameofOneChildAndIdOfOtherAreEqualSearchShouldReturnBothTheChildren() {
		String childNameAndUID = "1";
		
		Child child = new Child();
		child.setField("name", childNameAndUID);
		childrenStore.addOrUpdate(child);

		Child child2 = new Child();
		child2.setField("unique_identifier", childNameAndUID);
		childrenStore.addOrUpdate(child2);

		Child[] searchResults = childrenStore.search(childNameAndUID);

		Assert.assertEquals(searchResults.length, 2);
		Assert.assertEquals(searchResults[0], child);
		Assert.assertEquals(searchResults[1], child2);
	}
	
	@Ignore
	@Test
	public void shouldReturnChildrenArraySortedByNameIfSorterAttached() throws Exception {
		Child firstChild = new Child();
		firstChild.setField("name", "Tom");
		childrenStore.addOrUpdate(firstChild);
		
		Child secondChild = new Child();
		secondChild.setField("name", "Harry");
		childrenStore.addOrUpdate(secondChild);
		
		childrenStore.attachSorter(new ChildSorter(new String[] {"name"}));
		Children children = childrenStore.getAll();
		assertEquals(secondChild, children.toArray()[0]);
	}
	
	@Test
	public void shouldAttachASorterIfNullObjectIsPassed() throws Exception {
		childrenStore.attachSorter(null);
	}
	
}
