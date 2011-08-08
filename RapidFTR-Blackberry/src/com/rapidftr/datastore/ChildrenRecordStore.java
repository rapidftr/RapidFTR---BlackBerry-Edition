package com.rapidftr.datastore;

import java.util.Vector;

import com.rapidftr.model.Child;
import com.rapidftr.utilities.ChildSorter;
import com.rapidftr.utilities.Store;

public class ChildrenRecordStore {

	public static final String GET_ALL_CHILDREN_KEY = "children";
	private final Store store;
    private ChildSorter childSorter;

	public ChildrenRecordStore(Store store) {
		this(store, new ChildSorter());
	}


    public ChildrenRecordStore(Store store, ChildSorter childSorter) {
		this.store = store;
        this.childSorter = childSorter;
	}

	public synchronized void addOrUpdate(Child child) {
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
		return new Children(store.getVector(GET_ALL_CHILDREN_KEY), childSorter);
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

		return new Children(results, childSorter);
	}

	public Child getChildAt(int index) {
		Vector vector = store.getVector(GET_ALL_CHILDREN_KEY);
		return (Child) vector.elementAt(index);
	}

	public Children getAllSortedByName() {
		return getAll().sortBy(new StringField(Child.NAME), true);
	}

	public Children getAllSortedByRecentlyAdded() {
		return getAll().sortBy(new DateField(Child.CREATED_AT_KEY), false);
	}

	public Children getAllSortedByRecentlyUpdated() {
		return getAll().sortBy(new DateField(Child.LAST_UPDATED_KEY), false);
	}
}
