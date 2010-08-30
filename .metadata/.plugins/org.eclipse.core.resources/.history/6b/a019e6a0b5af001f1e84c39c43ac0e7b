package com.rapidftr.datastore;

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
	}

	public void addChild(Child child) {
		if (child == null) {
			return;
		}
		Vector children = (Vector) persistentStore.getContents();
		if (children == null) {
			children = new Vector();
		}
		children.addElement(child);
		persistentStore.setContents(children);
	}

	public Vector getAllChildren() {
		return (Vector) persistentStore.getContents();
	}

	public void setContents(Vector childrenList) {
		persistentStore.setContents(childrenList);
	}

}
