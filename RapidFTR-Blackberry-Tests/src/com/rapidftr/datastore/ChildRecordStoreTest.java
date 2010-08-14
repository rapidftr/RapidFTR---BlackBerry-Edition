package com.rapidftr.datastore;

import java.util.Vector;

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
		recordStore.addChild(child2);
		verify(childrenListOld).addElement(child2);
		verify(mockedPersistentStore).setContents(childrenListOld);
	}
	


}
