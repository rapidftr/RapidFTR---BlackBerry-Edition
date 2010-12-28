package com.rapidftr.datastore;

import com.rapidftr.model.Child;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ChildSorterTest {
    private Child child1;
    private Child child2;
    private Child child3;
    private Child[] childArray;
    private MockChildSorter testChildSorter;

    private SimpleDateFormat formatter;

    @Before
    public void SetUp() {
        child1 = new Child();
        child2 = new Child();
        child3 = new Child();
        childArray = new Child[] {child1, child2, child3};

        formatter = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
    }

    @Test
    public void shouldSort3RecordsByAlphabeticalOrder() {

        testChildSorter = new MockChildSorter(new String[] {"name"}, true);

        child1.setField("name","kiddo2");
        child2.setField("name","kiddo1");
        child3.setField("name","kiddo3");

        testChildSorter.sort(childArray);

        assertThat(compareChildArraysOnAttribute(new Child[] {child2, child1, child3}, childArray, "name"), is(true));
    }

    @Ignore
    @Test
    public void shouldSort3RecordsByDateCreated() {
        testChildSorter = new MockChildSorter(new String[] {"created_at"}, true);

        Calendar calendar = Calendar.getInstance();

        String today = formatter.format(new Date());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = formatter.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String dayBeforeYesterday = formatter.format(calendar.getTime());

        child1.setField("created_at",yesterday);
        child2.setField("created_at",today);
        child3.setField("created_at",dayBeforeYesterday);

        testChildSorter.sort(childArray);
        
        assertThat(compareChildArraysOnAttribute(new Child[]{child2, child1, child3}, childArray, "created_at"), is(true));
    }

    @Ignore
    @Test
    public void shouldSort3RecordsByDateModified() {
        testChildSorter = new MockChildSorter(new String[] {"last_updated_at"}, true);

        Calendar calendar = Calendar.getInstance();

        String today = formatter.format(new Date());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String yesterday = formatter.format(calendar.getTime());

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String dayBeforeYesterday = formatter.format(calendar.getTime());

        child1.setField("last_updated_at",yesterday);
        child2.setField("last_updated_at",today);
        child3.setField("last_updated_at",dayBeforeYesterday);

        testChildSorter.sort(childArray);

        assertThat(compareChildArraysOnAttribute(new Child[]{child2, child1, child3}, childArray, "last_updated_at"), is(true));
    }

    private boolean compareChildArraysOnAttribute (Child[] Expected, Child[] Actual, String attribute) {
        for (int index = 0; index < Expected.length ; index ++ ) {
            if(Expected[index].getField(attribute) != Actual[index].getField(attribute))
                return false;
        }
        return true;
    }
}
