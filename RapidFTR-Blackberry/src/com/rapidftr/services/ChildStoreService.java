package com.rapidftr.services;

import java.util.Vector;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;

public class ChildStoreService {
	private ChildrenRecordStore childRecordStore;

	public ChildStoreService(ChildrenRecordStore childRecordStore) {
		this.childRecordStore = childRecordStore;
	}

	public Child[] getAllChildrenFromPhoneStoredAsArray() {
		Vector children = childRecordStore.getAllChildren();
		Child[] childList = new Child[children.size()];
		children.copyInto(childList);
		return childList;
	}

	public Child[] searchChildrenFromStore(SearchChildFilter searchChildFilter) {
		Vector source = childRecordStore.getAllChildren();
		Vector results = new Vector();
		for (int i = 0; i < source.size(); i++) {
			Child child = (Child) source.elementAt(i);
			if ((!searchChildFilter.getName().equals("") && searchChildFilter
					.getName().equals(child.getField("name")))
					|| (!searchChildFilter.getId().equals("") && searchChildFilter
							.getName().equals(
									child.getField("unique_identifier")))) {
				

				results.addElement(child);
			}

		}
		Child resultsArray[] = new Child[results.size()];
		results.copyInto(resultsArray);
		return resultsArray;
		
	}

	public void syncChildWithStore(Child child) {
		childRecordStore.addOrUpdateChild(child);
	}


}
