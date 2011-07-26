package com.rapidftr.utilities;

import com.rapidftr.datastore.Field;
import com.rapidftr.model.Child;

public class ChildSorter {

    public void sort(Child[] children, final boolean isAscending, final Field field) {
        net.rim.device.api.util.Arrays.sort(children,
                new net.rim.device.api.util.Comparator() {
                    public int compare(Object o1, Object o2) {
                        return !isAscending ? field.compare((Child) o2,
                                (Child) o1) : field.compare((Child) o1,
                                (Child) o2);
                    }
                });
    }
}
