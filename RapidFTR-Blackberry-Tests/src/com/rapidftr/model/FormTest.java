package com.rapidftr.model;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class FormTest {

	
	@Test
	public void shouldCheckForEquals()
	{
		
	   Vector fieldList1 = new Vector();
	   fieldList1.add(new FormField("name","text"));
	   Form a = new Form("name","id",fieldList1);
	   
	   Vector fieldList2 = new Vector();
	   fieldList2.add(new FormField("name","text"));
	   Form b = new Form("name","id",fieldList2);
	   
	   assertTrue(a.equals(b));
	   
	   Form c = new Form("name","idc",new Vector());
	   assertFalse(a.equals(c));
	   
	   Form d = new Form("name","idc",fieldList1);
	   
	   assertFalse(d.equals(c));
	   
	}
	
}
