package com.rapidftr.datastore;

import static org.junit.Assert.*;

import com.rapidftr.model.ChildFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.rapidftr.model.Child;

public class ChildComparatorTest {

	private static final String CHILD_NAME_KEY = "name";
	private static final String CHILD_LAST_KNOWN_LOCATION_KEY = "last_known_location";
	private ChildComparator childComparator;
	private Child firstChild;
	private Child secondChild;

	@Before
	public void setup() {
		childComparator = new ChildComparator();
		firstChild = ChildFactory.newChild();
		secondChild = ChildFactory.newChild();
	}
	
	@Test
	public void shouldSortByNameIfEmptyAttributesListIsPassed() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Tom");
		secondChild.setField(CHILD_NAME_KEY, "Harry");
		
		Assert.assertFalse(childComparator.compare(firstChild, secondChild) == 0);
	}
	
	@Test
	public void shouldNotReturnZeroIfChildrenSortedOnName() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Tom");
		secondChild.setField(CHILD_NAME_KEY, "Harry");
		
		childComparator.setAttributes(new String[] { CHILD_NAME_KEY });
		Assert.assertFalse(childComparator.compare(firstChild, secondChild) == 0);
	}
	
	@Test
	public void shouldNotReturnZeroIfChildrenSortedOnLocation() throws Exception {
		firstChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "bangalore");
		secondChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "Australia");

		childComparator.setAttributes(new String[] {CHILD_LAST_KNOWN_LOCATION_KEY});
		Assert.assertFalse(childComparator.compare(firstChild, secondChild) == 0);
	}
	
	@Test
	public void shouldNotReturnZeroIfChildrenWithSameNameSortedOnNameAndLocation() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Tom");
		firstChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "bangalore");

		secondChild.setField(CHILD_NAME_KEY, "Tom");
		secondChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "Australia");

		childComparator.setAttributes(new String[] {CHILD_NAME_KEY, CHILD_LAST_KNOWN_LOCATION_KEY});
		Assert.assertFalse(childComparator.compare(firstChild, secondChild) == 0);
	}
	
	@Test
	public void shouldReturnOneIfLocationOfFirstChildIsLexicallyLowerThanTheLocationOfOther() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Tom");
		firstChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "bangalore");

		secondChild.setField(CHILD_NAME_KEY, "Tom");
		secondChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "Australia");

		childComparator.setAttributes(new String[] {CHILD_NAME_KEY, CHILD_LAST_KNOWN_LOCATION_KEY});
		Assert.assertTrue(childComparator.compare(firstChild, secondChild) > 0);
	}
	
	@Test
	public void shouldSortFirstByNameThenByLocationIfNameAttributeIsPassedBeforeLocationAttribute() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Harry");
		firstChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "bangalore");

		secondChild.setField(CHILD_NAME_KEY, "Tom");
		secondChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "Australia");

		childComparator.setAttributes(new String[] {CHILD_NAME_KEY, CHILD_LAST_KNOWN_LOCATION_KEY});
		Assert.assertTrue(childComparator.compare(firstChild, secondChild) < 0);
	}
	
	@Test
	public void shouldSortFirstByLocationThenByNameIfLocationAttributeIsPassedBeforeNameAttribute() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Harry");
		firstChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "bangalore");

		secondChild.setField(CHILD_NAME_KEY, "Tom");
		secondChild.setField(CHILD_LAST_KNOWN_LOCATION_KEY, "Australia");

		childComparator.setAttributes(new String[] {CHILD_LAST_KNOWN_LOCATION_KEY, CHILD_NAME_KEY});
		Assert.assertTrue(childComparator.compare(firstChild, secondChild) > 0);
	}
	
	@Test
	public void shouldSortOnlyByNameIfLocationAttributeIsNotSetAndItIsPassedBeforeNameAttribute() throws Exception {
		firstChild.setField(CHILD_NAME_KEY, "Zorro");
		secondChild.setField(CHILD_NAME_KEY, "Tom");

		childComparator.setAttributes(new String[] {CHILD_LAST_KNOWN_LOCATION_KEY, CHILD_NAME_KEY});
		Assert.assertTrue(childComparator.compare(firstChild, secondChild) > 0);
	}
	
	@Test
	public void shouldReturnZeroIfLocationAttributeAndNameAttributeAreNotSet() throws Exception {
		childComparator.setAttributes(new String[] {CHILD_LAST_KNOWN_LOCATION_KEY, CHILD_NAME_KEY});
		Assert.assertTrue(childComparator.compare(firstChild, secondChild) == 0);
	}
}
