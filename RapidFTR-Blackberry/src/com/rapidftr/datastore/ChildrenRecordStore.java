package com.rapidftr.datastore;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.rapidftr.model.Child;

public class ChildrenRecordStore {

	private static final long KEY = 0x63ce3e890db0296dL; // com.rapidftr.datastore.ChildRecordStore
	PersistentStore persistentStore;

	public ChildrenRecordStore() {
		initilaize();
	}

	protected void initilaize() {
		persistentStore = new PersistentStore(KEY);

		if (persistentStore.getContents() == null) {
			persistentStore.setContents(new Vector());

		}

	}

	public void addOrUpdateChild(Child child) {
		if (child == null) {
			return;
		}

		Vector children = (Vector) persistentStore.getContents();

		if (children == null) {
			children = new Vector();
		}
		if (children.contains(child)) {
			children.setElementAt(child, children.indexOf(child));
		} else {
			children.addElement(child);
		}
		persistentStore.setContents(children);
	}

	public Vector getAllChildren() {
		return (Vector) persistentStore.getContents();
	}


	public void storeChildren(Vector childrenList) {
		Enumeration enumeration = childrenList.elements();
		while (enumeration.hasMoreElements())
			addOrUpdateChild((Child) enumeration.nextElement());
	}

	public void deleteAllChildren() {
		persistentStore.setContents(new Vector());
	}

}
