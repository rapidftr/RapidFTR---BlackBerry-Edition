package com.rapidftr.form;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;

public class FormsTest {
	@Test
	public void addForm() throws Exception {
		Forms forms = new Forms(new JSONArray(oneForm()));
		final boolean[] hasForm = { false };
		forms.forEachForm(new FormAction() {
			@Override
			public void execute(com.rapidftr.form.Form form) {
				hasForm[0] = true;
				assertNotNull(form);
			}
		});
		assertTrue(hasForm[0]);
	}

	@Test
	public void addAnotherForm() throws JSONException {
		Forms forms = new Forms(new JSONArray(twoForms()));
		final int[] formCount = { 0 };
		forms.forEachForm(new FormAction() {
			@Override
			public void execute(com.rapidftr.form.Form form) {
				formCount[0]++;
				assertNotNull(form);
			}
		});
		assertEquals(2, formCount[0]);
	}

	@Test
	public void doNotAddInvalidForms() throws Exception {
		JSONArray array = Mockito.mock(JSONArray.class);
		Mockito.when(array.length()).thenReturn(1);
		Mockito.when(array.getJSONObject(0)).thenThrow(new JSONException(""));
		new Forms(array).forEachForm(new FormAction() {
			@Override
			public void execute(Form form) {
				fail("Should not add invalid forms");
			}
		});
	}

	@Test
	public void verifyFormProperties() throws Exception {
		final JSONArray formsArray = new JSONArray(twoForms());
		Forms forms = new Forms(formsArray);
		forms.forEachForm(new FormAction() {
			int i = 0;

			@Override
			public void execute(com.rapidftr.form.Form form) {
				try {
					assertEquals(formsArray.getJSONObject(i).get("name")
							.toString(), form.getName());
					assertEquals(formsArray.getJSONObject(i).get("unique_id")
							.toString(), form.getUniqueId());
					assertTrue(form.isEnabled());
					i++;
				} catch (JSONException e) {
					fail();
				}
			}
		});
	}

	@Test
	public void forEachField() throws Exception {
		Forms forms = new Forms(new JSONArray(
				"[{'name':'Basic Details','unique_id':'basic_details', 'enabled':true , "
						+ "fields: " + "[{'name':'name'," + "'enabled':true,"
						+ "'type':'text_field'," + "'display_name':'Name'}]}]"));
		final boolean[] hasField = { false };
		forms.forEachField(new FormFieldAction() {
			@Override
			public void execute(FormField field) {
				hasField[0] = true;
			}
		});
		assertTrue(hasField[0]);
	}

	@Test
	public void doNotIncludeDisabledForms() throws Exception {
		final JSONArray formsArray = new JSONArray(oneDisabledForm());
		Forms forms = new Forms(formsArray);
		forms.forEachForm(new FormAction() {
			@Override
			public void execute(com.rapidftr.form.Form form) {
				fail("Should ignore disabled forms");
			}
		});
	}

	private String twoForms() {
		return "[{'name':'Basic Details','unique_id':'basic_details', 'enabled':true},{'name':'Family Details','unique_id':'family_details', 'enabled':true}]";
	}

	private String oneForm() {
		return "[{'name':'Basic Details','unique_id':'basic_details', 'enabled':true}]";
	}
	
	private String oneDisabledForm() {
		return "[{'name':'Basic Details','unique_id':'basic_details', 'enabled':false}]";
	}
}
