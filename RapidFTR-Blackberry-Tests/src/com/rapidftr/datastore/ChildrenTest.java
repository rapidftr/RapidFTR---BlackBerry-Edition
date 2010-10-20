package com.rapidftr.datastore;

import static org.junit.Assert.assertEquals;
import java.util.Vector;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.rapidftr.model.Child;

public class ChildrenTest {
	private Vector childrenVector;
	private Child firstChild;
	private Child secondChild;
	
	@Before
	public void setup() {
		childrenVector = new Vector();
		
		firstChild = new Child();
		firstChild.setField("name", "Tom");
		childrenVector.addElement(firstChild);
		
		secondChild = new Child();
		secondChild.setField("name", "Harry");
		childrenVector.addElement(secondChild);
	}
	
	@Ignore
	@Test
	public void shouldReturnChildrenArraySortedByName() throws Exception {
		Children children = new Children(childrenVector);
		Child[] childrenArray = children.toArray();
		assertEquals(secondChild, childrenArray[0]);
	}
}
