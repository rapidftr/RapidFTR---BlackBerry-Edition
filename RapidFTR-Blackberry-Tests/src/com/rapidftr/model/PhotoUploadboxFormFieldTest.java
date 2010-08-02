package com.rapidftr.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class PhotoUploadboxFormFieldTest {

	
	@Test
	public void shouldCheckForEquals()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		
		FormField field1 = formFieldFactory.createFormField("photo", "photo_upload_box", null);
		FormField field2 = formFieldFactory.createFormField("photo", "photo_upload_box", null);
		assertEquals(field1,field2);
	}
}
