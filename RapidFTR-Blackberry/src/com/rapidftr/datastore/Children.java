package com.rapidftr.datastore;

import java.util.Enumeration;
import java.util.Vector;
import net.rim.device.api.util.Arrays;
import net.rim.device.api.util.Comparator;
import com.rapidftr.model.Child;

public class Children {

	private final Vector vector;

	public Children(Vector vector) {
		this.vector = vector;
	}

	public int count() {
		return vector.size();
	}

	public void forEachChild(ChildAction action) {
		Enumeration elements = vector.elements();
		while(elements.hasMoreElements()){
			Child child = (Child)elements.nextElement();
			action.execute(child);
		}
	}

	public Child[] toArray() {
		Child[] array = new Child[count()];
		vector.copyInto(array);
		sortByNameAndLocation(array);
		return array;
	}

	private void sortByNameAndLocation(Child[] childList) {
		Arrays.sort(childList, new Comparator() {
			public int compare(Object o1, Object o2) {
				Child child1 = (Child) o1;
				Child child2 = (Child) o2;
				int nameComparator = stringIgnoreCaseComparator((String) child1
						.getField("name"), (String) child2
						.getField("name"));
				if (nameComparator == 0) {
					return stringIgnoreCaseComparator((String) child1
							.getField("last_known_location"), (String) child2
							.getField("last_known_location"));
				}

				return nameComparator;
			}

		});
	}

	private int stringIgnoreCaseComparator(String firstString,
			String secondString) {
		int nameComparator = (firstString.equals(null) ? firstString
				: firstString.toLowerCase()).compareTo((secondString
				.equals(null) ? secondString : secondString.toLowerCase()));
		return nameComparator;
	}

}
