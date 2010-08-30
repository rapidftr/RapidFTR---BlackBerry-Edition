package com.rapidftr.services;

import java.util.Vector;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;

public class ChildStoreService {
	private ChildrenRecordStore childRecordStore;

	public ChildStoreService(ChildrenRecordStore childRecordStore) {
		this.childRecordStore = childRecordStore;
	}

	public Child[] getAllChildrenFromPhoneStoredAsArray() {
		Vector children = childRecordStore.getAllChildren();
		Child[] childList = new Child[children.size()];
		for (int i = 0; i < children.size(); i++) {
			childList[i] = (Child) children.elementAt(i);
		}
		return childList;
	}

}
