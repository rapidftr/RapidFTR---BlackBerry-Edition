package com.rapidftr.model;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;


public class FormFieldTest {
	
	@Test
	public void shouldCheckForEquals()
	{
		FormField formField1 = new FormField("name", "type");
		FormField formField2 = new FormField("name","type"); 
		assertEquals(formField1,formField2);
	}
	
	@Test
	public void equalsShouldCheckForOptionStringsAreEqual()
	{
		Vector optionStrings1 = new Vector();
		optionStrings1.add("option1");
		optionStrings1.add("option2");
		
		FormField formField1 = new FormField("name", "type",optionStrings1);
		
		Vector optionStrings2 = new Vector();
		optionStrings1.add("option1");
		optionStrings1.add("option");
		
		FormField formField2 = new FormField("name", "type",optionStrings2);
		
		assertFalse(formField1.equals(formField2));
		
	}

}
