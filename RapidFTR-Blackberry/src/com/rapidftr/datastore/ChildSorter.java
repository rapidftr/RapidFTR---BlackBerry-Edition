package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public class ChildSorter {

	private String[] attributes;

	public ChildSorter(String[] attributes) {
		this.attributes = attributes;
	}
	
	public void sort(Child[] array) {
		net.rim.device.api.util.Arrays.sort(array, getComparator());
	}

	private net.rim.device.api.util.Comparator getComparator() {
		return new net.rim.device.api.util.Comparator() {
			public int compare(Object o1, Object o2) {
				ChildComparator childComparator = new ChildComparator();
				childComparator.setAttributes(attributes);
				return childComparator.compare((Child)o1, (Child)o2);
			}
		};
	}
	
}
