package com.rapidftr.datastore;

import com.rapidftr.model.Child;
import com.rapidftr.utilities.Store;

import java.util.Vector;

public class ChildrenRecordStore {

	private static final String GET_ALL_CHILDREN_KEY = "children";
	private final Store store;
	private ChildSorter sorter;

	public ChildrenRecordStore(Store store) {
		this.store = store;
	}

	public void addOrUpdate(Child child) {
		if (child == null) {
			return;
		}

		Vector children = store.getVector(GET_ALL_CHILDREN_KEY);

		if (children.contains(child)) {
			children.setElementAt(child, children.indexOf(child));
		} else {
			children.addElement(child);
		}
		
		store.setVector(GET_ALL_CHILDREN_KEY, children);
	}

	public Children getAll() {
		Child[] array = new Children(store.getVector(GET_ALL_CHILDREN_KEY)).toArray();
		sort(array);
		return new Children(array);
	}

	public void deleteAll() {
		store.clear();
	}

	public Child[] getAllAsArray() {
		return getAll().toArray();
	}

	public Child[] search(final String query) {
		final Vector results = new Vector();
		getAll().forEachChild(new ChildAction() {

			public void execute(Child child) {
				if (child.matches(query.toLowerCase())) {
					results.addElement(child);
				}
			}
		});

		return new Children(results).toArray();
	}

	public void attachSorter(ChildSorter sorter) {
		if(sorter!=null)
			this.sorter = sorter;
		else
			this.sorter = new ChildSorter(new String[] {"name"});
	}
	
	private void sort(Child[] array) {
		if(sorter!=null)
			sorter.sort(array, true);
	}


}
