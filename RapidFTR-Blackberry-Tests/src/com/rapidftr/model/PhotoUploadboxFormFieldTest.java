package com.rapidftr.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhotoUploadboxFormFieldTest {

	
	@Test
	public void shouldCheckForEquals()
	{
		FormFieldFactory formFieldFactory = new FormFieldFactory();
		
		FormField field1 = formFieldFactory.createFormField("photo", "Child's photo", "photo_upload_box", null, "");
		FormField field2 = formFieldFactory.createFormField("photo", "Child's photo", "photo_upload_box", null, "");
		assertEquals(field1,field2);
	}
}
