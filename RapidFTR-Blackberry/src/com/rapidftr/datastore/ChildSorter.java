package com.rapidftr.datastore;

import com.rapidftr.model.Child;

public class ChildSorter {

	protected String[] attributes;

	public ChildSorter(String[] attributes) {
		this.attributes = attributes;
	}
	
	public void sort(Child[] array, boolean isAscending) {
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
