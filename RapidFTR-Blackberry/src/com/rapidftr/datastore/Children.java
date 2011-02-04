package com.rapidftr.datastore;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.collection.List;

import com.rapidftr.model.Child;

public class Children {

	private final Vector vector;

	public Children(Vector vector) {
		this.vector = vector;
	}

	public Children(Child[] array) {
		vector = new Vector();
		for (int i = 0; i < array.length; i++) {
			vector.addElement(array[i]);
		}
	}

	public int count() {
		return vector.size();
	}

	public void forEachChild(ChildAction action) {
		Enumeration elements = vector.elements();
		while (elements.hasMoreElements()) {
			Child child = (Child) elements.nextElement();
			action.execute(child);
		}
	}

	public Child[] toArray() {
		Child[] array = new Child[count()];
		vector.copyInto(array);
		return array;
	}


	protected Children sortBy(final Field field, final boolean isAscending) {
		Child[] children = toArray();
		net.rim.device.api.util.Arrays.sort(children,
				new net.rim.device.api.util.Comparator() {
					public int compare(Object o1, Object o2) {
						return !isAscending ? field.compare((Child) o2,
								(Child) o1) : field.compare((Child) o1,
								(Child) o2);
					}
				});
		return new Children(children);
	}

	public Object[] getChildrenAndImages(int size) {
		int initialSize = size <= count() ? size : count();
		Object[] childrenAndImages = new Object[initialSize];
		for (int i = 0; i < initialSize; i++) {
			Child child = (Child) vector.elementAt(i);
			childrenAndImages[i] = child.toImagepair();
		}
		return childrenAndImages;
	}
}
