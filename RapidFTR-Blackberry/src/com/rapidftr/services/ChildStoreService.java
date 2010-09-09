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
		if(children == null)
		{
			return new Child[0];
		}
		Child[] childList = new Child[children.size()];
		children.copyInto(childList);
		return childList;
	}

	public Child[] searchChildrenFromStore(SearchChildFilter searchChildFilter) {
		Vector source = childRecordStore.getAllChildren();
		Vector results = new Vector();
		String queryString = searchChildFilter.getName().toLowerCase();

		
		for (int i = 0; i < source.size(); i++) {

			Child child = (Child) source.elementAt(i);
			
			String id = (String)child.getField("unique_identifier");
			if(id == null)
			{
				id="";
			}
			
			String name = (String) child.getField("name");
			if(name==null)
			{
				name="";
			}
			
			if ((!"".equals(queryString)
					&& (id.equalsIgnoreCase(queryString)))||(!"".equals(queryString) && name.toString().toLowerCase().indexOf(queryString) != -1)) {

				results.addElement(child);
			}

		}

		Child resultsArray[] = new Child[results.size()];
		results.copyInto(resultsArray);
		return resultsArray;

	}


	public void saveChildInLocalStore(Child child) {
		childRecordStore.addOrUpdateChild(child);
	}

	public void saveChildrenInLocalStore(Vector children){	
		childRecordStore.storeChildren(children);		
	}

}
