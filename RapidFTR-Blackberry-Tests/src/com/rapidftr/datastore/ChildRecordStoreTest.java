package com.rapidftr.datastore;

import static org.junit.Assert.assertEquals;

import com.rapidftr.model.ChildFactory;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.rapidftr.model.Child;

public class ChildRecordStoreTest {

	private ChildrenRecordStore childrenStore;

	@Before
	public void setup() {
		childrenStore = new ChildrenRecordStore(new MockStore());
	}	

	@Test
	public void saveShouldAppendNewChildAndSaveInStore() {
		int count = childrenStore.getAll().count();
		childrenStore.addOrUpdate(ChildFactory.newChild());
		childrenStore.addOrUpdate(ChildFactory.newChild());
		Assert.assertEquals(count + 2, childrenStore.getAll().count());
	}

	@Test
	public void saveShouldUpdateTheChildIfItExists() {
		Child childOne = ChildFactory.newChild();
		childOne.setField("name", "NewChild");
		childOne.setField("_id", "1");
		childrenStore.addOrUpdate(childOne);
		int initial = childrenStore.getAll().count();

		Child updatedChildOne = ChildFactory.newChild();
		updatedChildOne.setField("name", "UpdatedChild");
		updatedChildOne.setField("_id", "1");
		childrenStore.addOrUpdate(updatedChildOne);

		Assert.assertEquals(initial, childrenStore.getAll().count());
	}

	@Test
	public void ifPersistentStoreIsEmptySearchShouldResultAnEmptyArray() {
		Assert.assertEquals(childrenStore.search("Tom").count(), 0);
	}
	
	@Test
	public void ifChildIsNotPresentInPersistentStoreSearchShouldReturnEmptyResults() {
		Assert.assertEquals(childrenStore.search("Harry").count(), 0);
	}
	
	@Test
	public void ifChildWithNamePresentInPersistentStoreSearchShouldReturnTheChild() {
		Child Tom = ChildFactory.newChild();
		String childName = "Tom";
		Tom.setField("name", childName);
		childrenStore.addOrUpdate(Tom);
		
		Children searchResults = childrenStore.search(childName);

		Assert.assertEquals(searchResults.count(), 1);
		Assert.assertEquals(searchResults.toArray()[0], Tom);
	}
	
	@Test
	public void searchShouldReturnAllTheChildrenWithSearchedName() {
		String childName = "Tom";
		Child child = ChildFactory.newChild();
		child.setField("name", childName);
		childrenStore.addOrUpdate(child);
		
		Child child2 = ChildFactory.newChild();
		child2.setField("name", childName);
		childrenStore.addOrUpdate(child2);
		
		Children searchResults = childrenStore.search(childName);

		Assert.assertEquals(searchResults.count(), 2);
		Assert.assertEquals(searchResults.toArray()[0], child);
		Assert.assertEquals(searchResults.toArray()[1], child2);
	}
	
	@Test
	public void ifChildWithIdPresentInPersistentStoreSearchShouldReturnTheChild() {
		Child child = ChildFactory.newChild();
		child.setField("name", "Tom");
		String childUID = "1";
		child.setField("unique_identifier", childUID);
		childrenStore.addOrUpdate(child);
		
		Children searchResults = childrenStore.search(childUID);

		Assert.assertEquals(searchResults.count(), 1);
		Assert.assertEquals(searchResults.toArray()[0], child);
	}

	@Test
	public void ifNameofOneChildAndIdOfOtherAreEqualSearchShouldReturnBothTheChildren() {
		String childNameAndUID = "1";
		
		Child child = ChildFactory.newChild();
		child.setField("name", childNameAndUID);
		childrenStore.addOrUpdate(child);

		Child child2 = ChildFactory.newChild();
		child2.setField("unique_identifier", childNameAndUID);
		childrenStore.addOrUpdate(child2);

		Children searchResults = childrenStore.search(childNameAndUID);

		Assert.assertEquals(searchResults.count(), 2);
		Assert.assertEquals(searchResults.toArray()[0], child);
		Assert.assertEquals(searchResults.toArray()[1], child2);
	}

}
