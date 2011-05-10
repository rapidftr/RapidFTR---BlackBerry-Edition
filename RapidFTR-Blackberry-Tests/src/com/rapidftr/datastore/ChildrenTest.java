package com.rapidftr.datastore;

import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Locale;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ChildrenTest {
	private final class MockDateField extends DateField {
		private MockDateField(String attribute) {
			super(attribute);
		}

		@Override
		protected long parse(String date) {
			try {
				return formatter.parse(date).getTime();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	private Child child1;
	private Child child2;
	private Child child3;
	private Child[] childArray;

	private SimpleDateFormat formatter;
	private Children children;

	@Before
	public void SetUp() {
		child1 = ChildFactory.newChild();
		child2 = ChildFactory.newChild();
		child3 = ChildFactory.newChild();
		childArray = new Child[] { child1, child2, child3 };
		children = new Children(childArray) {
			@Override
			public Children sortBy(final Field field,
					final boolean isAscending) {
				Child[] children = toArray();
				Arrays.sort(children, new Comparator() {
					public int compare(Object o1, Object o2) {
						return !isAscending ? field.compare((Child) o2,
								(Child) o1) : field.compare((Child) o1,
								(Child) o2);
					}
				});
				return new Children(children);

			}
		};

		formatter = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
	}

	@Test
	public void shouldSort3RecordsByAlphabeticalOrder() {

		child1.setField("name", "bcef");
		child2.setField("name", "aexe");
		child3.setField("name", "zeid");

		assertThat(compareChildArraysOnAttribute(new Child[] { child2, child1,
				child3 }, children.sortBy(new StringField("name"), true)
				.toArray(), "name"), is(true));
	}

	@Test
	public void shouldSort3RecordsByDateCreated() {

		Calendar calendar = Calendar.getInstance();
		calendar.set(2011, 2, 2);

		String today = formatter.format(calendar.getTime());

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String yesterday = formatter.format(calendar.getTime());

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String dayBeforeYesterday = formatter.format(calendar.getTime());

		child1.setField("created_at", yesterday);
		child2.setField("created_at", today);
		child3.setField("created_at", dayBeforeYesterday);

		assertThat(compareChildArraysOnAttribute(new Child[] { child2, child1,
				child3 }, children.sortBy(new MockDateField("created_at"), false)
				.toArray(), "created_at"), is(true));
	}

	@Test
	public void shouldSort3RecordsByDateModified() {

		Calendar calendar = Calendar.getInstance();

		calendar.set(2011, 2, 2);

		String today = formatter.format(calendar.getTime());

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String yesterday = formatter.format(calendar.getTime());

		calendar.add(Calendar.DAY_OF_YEAR, -1);
		String dayBeforeYesterday = formatter.format(calendar.getTime());

		child2.setField("last_updated_at", today);
		child1.setField("last_updated_at", yesterday);
		child3.setField("last_updated_at", dayBeforeYesterday);

		assertThat(compareChildArraysOnAttribute(new Child[] { child2, child1,
				child3 }, children.sortBy(new MockDateField("last_updated_at"),
				false).toArray(), "last_updated_at"), is(true));
	}

	private boolean compareChildArraysOnAttribute(Child[] Expected,
			Child[] Actual, String attribute) {
		for (int index = 0; index < Expected.length; index++) {
			if (Expected[index].getField(attribute) != Actual[index]
					.getField(attribute))
				return false;
		}
		return true;
	}
}
