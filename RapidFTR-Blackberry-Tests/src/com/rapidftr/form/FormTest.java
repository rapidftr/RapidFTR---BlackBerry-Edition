package com.rapidftr.form;

import static org.junit.Assert.*;

import org.json.me.JSONObject;
import org.junit.Test;

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
