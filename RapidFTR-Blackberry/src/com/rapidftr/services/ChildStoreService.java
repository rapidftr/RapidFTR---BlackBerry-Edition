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
		String queryString = searchChildFilter.getName().toLowerCase();
//		String childId = searchChildFilter.getId().toLowerCase();

		
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


//	private void searchByName(Vector results, Vector source, String childName) {
//		for (int i = 0; i < source.size(); i++) {
//
//			Child child = (Child) source.elementAt(i);
//			
//			String field = (String)child.getField("unique_identifier");
//			String name = child.getField("name").toString().toLowerCase();
//			
//			if ((!"".equals(childName) && name.indexOf(childName) != -1)) {
//
//				results.addElement(child);
//			}
//
//		}
//
//	}
//
//	private void searchById(Vector results, Vector source, String childId) {
//		for (int i = 0; i < source.size(); i++) {
//
//			Child child = (Child) source.elementAt(i);
//
//			String field = (String)child.getField("unique_identifier");
//			if(field==null)
//			{
//				continue;
//			}
//			if (!"".equals(childId)
//					&& (field.equalsIgnoreCase(childId))) {
//
//				results.addElement(child);
//			}
//
//		}
//	}

	public void syncChildWithStore(Child child) {
		childRecordStore.addOrUpdateChild(child);
	}


}
