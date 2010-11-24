package com.rapidftr.datastore;

import com.rapidftr.model.Child;
import org.junit.Ignore;

import java.util.Arrays;
import java.util.Comparator;

@Ignore
public class TestChildSorter extends ChildSorter{

    public TestChildSorter(String[] attributes) {
        super(attributes);
    }

    @Override
    public void sort(Child[] array, boolean isAscending){
	    Arrays.sort(array, getComparator(isAscending));
    }

    private Comparator getComparator(final boolean isAscending) {
		return new Comparator() {
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
