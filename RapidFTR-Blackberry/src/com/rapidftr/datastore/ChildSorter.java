package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public class ChildSorter {

	protected String[] attributes;
	private boolean isAscending;

	public ChildSorter(String[] attributes, boolean isAscending) {
		this.attributes = attributes;
		this.isAscending = isAscending;
	}
	
	public void sort(Child[] array) {
		net.rim.device.api.util.Arrays.sort(array, getComparator(isAscending));
	}

	private net.rim.device.api.util.Comparator getComparator(final boolean isAscending) {
		return new net.rim.device.api.util.Comparator() {
			public int compare(Object o1, Object o2) {
				ChildComparator childComparator = new ChildComparator();
				childComparator.setAttributes(attributes);
                return isAscending ?
                        childComparator.compare((Child)o1, (Child)o2) :
                        childComparator.compare((Child)o2 , (Child)o1);
			}
		};
	}
}
