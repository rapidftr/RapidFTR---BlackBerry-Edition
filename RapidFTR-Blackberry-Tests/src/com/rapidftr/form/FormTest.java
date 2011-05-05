package com.rapidftr.form;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Test;
import org.mockito.Mockito;

public class FormTest {
	@Test
	public void addField() throws Exception {
		Form form = new Form(new JSONObject(oneField()));
		final boolean hasField[] = { false };
		form.forEachField(new FormFieldAction() {

			@Override
			public void execute(FormField field) {
				hasField[0] = true;
				assertNotNull(field);
			}
		});
		assertTrue(hasField[0]);
		assertTrue(form.isNotEmpty());
	}
	
	@Test
	public void emptyWhenNoField() throws Exception {
		Form form = new Form(new JSONObject("{'fields':[]}"));
		assertFalse(form.isNotEmpty());
	}
	
	@Test
	public void toStringReturnsName() throws Exception {
		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		new Form(jsonObject).toString();
		Mockito.verify(jsonObject).getString("name");
	}
	
	@Test
	public void ignoreInvalidFields() throws Exception {
		JSONObject jsonObject = Mockito.mock(JSONObject.class);
		Mockito.when(jsonObject.getString("fields")).thenThrow(
				new JSONException(""));
		Form form = new Form(jsonObject);
		form.forEachField(new FormFieldAction() {
			@Override
			public void execute(FormField field) {
				fail("Should ignore invalid fields");
			}
		});
		assertFalse(form.isNotEmpty());
	}

	@Test
	public void verifyField() throws Exception {
		JSONObject jsonOne = new JSONObject(oneField());

		Form form = new Form(jsonOne);
		form.forEachField(new FormFieldAction() {

			@Override
			public void execute(FormField field) {
				assertEquals("age", field.getName());
				assertTrue(field.isEnabled());
				assertEquals("numeric_field", field.getType());
				assertEquals("age", field.getDisplayName());
			}
		});
	}

	private String oneField() {
		return "{'fields':[{'name':'age','enabled':true,'type':'numeric_field','display_name':'age'}]}";
	}
}
