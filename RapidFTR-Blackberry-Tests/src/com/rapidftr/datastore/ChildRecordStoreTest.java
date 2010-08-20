package com.rapidftr.datastore;

import java.util.Vector;

import junit.framework.Assert;

import org.junit.Test;

import com.rapidftr.model.Child;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ChildRecordStoreTest {

	@Test
	public void saveShouldGetListOfChildrenFromPersistentStoreAndAppendTheNewChildAndPersistTheUpdataedList() {
		final PersistentStore mockedPersistentStore = mock(PersistentStore.class);

		ChildrenRecordStore recordStore = new ChildrenRecordStore() {
			protected void initilaize() {
				persistentStore = mockedPersistentStore;
			}
		};

		Vector childrenListOld = mock(Vector.class);
		when(mockedPersistentStore.getContents()).thenReturn(childrenListOld);
		Child child2 = mock(Child.class);
		recordStore.addOrUpdateChild(child2);
		verify(childrenListOld).addElement(child2);
		verify(mockedPersistentStore).setContents(childrenListOld);
	}
	

	
	@Test
	public void saveShouldGetListOfChildrenFromPersistentStoreAndUpdateTheChildIfExists() {
		
		final PersistentStore mockedPersistentStore = mock(PersistentStore.class);

		ChildrenRecordStore recordStore = new ChildrenRecordStore() {
			protected void initilaize() {
				persistentStore = mockedPersistentStore;
			}
		};

		Vector childrenListOld = new Vector();
		when(mockedPersistentStore.getContents()).thenReturn(childrenListOld);
		when(recordStore.getAllChildren()).thenReturn(childrenListOld);
		Child newChild = new Child();
		newChild.setField("name", "NewChild");
		newChild.setField("_id", "1");
		recordStore.addOrUpdateChild(newChild);
		Child updatedChild = new Child();
		updatedChild.setField("name", "UpdatedChild");
		updatedChild.setField("_id", "1");		
		recordStore.addOrUpdateChild(updatedChild);
		Assert.assertEquals(1,recordStore.getAllChildren().size());
	}
	

}
