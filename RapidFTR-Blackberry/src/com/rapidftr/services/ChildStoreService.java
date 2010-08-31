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
		String childName = searchChildFilter.getName().toLowerCase();
		String childId = searchChildFilter.getId().toLowerCase();

		//If name empty search by id
		if("".equals(childName) && !"".equals(childId))
		{
			searchById(results, source, childId);
		}
		
		//If name empty search by Name
		else if("".equals(childId)&& !"".equals(childName))
		{
			searchByName(results, source, childName);
		}
		//If none empty then search only on ID
		else if(!"".equals(childId)&& !"".equals(childName))
		{
			searchById(results, source, childId);
		}
		
		Child resultsArray[] = new Child[results.size()];
		
		for (int i = 0; i < results.size(); i++) {
			resultsArray[i] = (Child) results.elementAt(i);
		}
		return resultsArray;

	}

	private void searchByName(Vector results, Vector source, String childName) {
		for (int i = 0; i < source.size(); i++) {

			Child child = (Child) source.elementAt(i);

			if ((!"".equals(childName) && child.getField("name").toString()
					.toLowerCase().indexOf(childName) != -1)) {

				results.addElement(child);
			}

		}

	}

	private void searchById(Vector results, Vector source, String childId) {
		for (int i = 0; i < source.size(); i++) {

			Child child = (Child) source.elementAt(i);

			String field = (String)child.getField("unique_identifier");
			if(field==null)
			{
				continue;
			}
			if (!"".equals(childId)
					&& (field.equalsIgnoreCase(childId))) {

				results.addElement(child);
			}

		}
	}

}
