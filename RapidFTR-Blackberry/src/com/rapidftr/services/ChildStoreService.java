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
		for (int i = 0; i < children.size(); i++) {
			childList[i] = (Child) children.elementAt(i);
		}
		return childList;
	}

	public Child[] searchChild(SearchChildFilter searchChildFilter) {
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
		for (int i = 0; i < results.size(); i++) {
			resultsArray[i] = (Child) results.elementAt(i);
		}
		return resultsArray;
		
	}


}
