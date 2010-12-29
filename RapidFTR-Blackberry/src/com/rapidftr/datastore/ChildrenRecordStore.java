package com.rapidftr.datastore;

import com.rapidftr.model.Child;
import com.rapidftr.utilities.Store;

import java.util.Vector;

public class ChildrenRecordStore {

	private static final String GET_ALL_CHILDREN_KEY = "children";
	private final Store store;
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
		return getChildren();
	}

	private Children getChildren() {
		return new Children(store.getVector(GET_ALL_CHILDREN_KEY));
	}

	
	public void deleteAll() {
		store.clear();
	}

	public Children search(final String query) {
		final Vector results = new Vector();
		getAll().forEachChild(new ChildAction() {

			public void execute(Child child) {
				if (child.matches(query.toLowerCase())) {
					results.addElement(child);
				}
			}
		});

		return new Children(results);
	}

	public Children getAllSortedByRecentlyAdded() {
		return getChildren().sortRecentlyAdded();
	}

	public Children getAllSortedByRecentlyUpdated() {
		return getChildren().sortRecentlyUpdated();
	}

	public Children getAllSortedByName() {
		return getChildren().sortName();
	}

}
