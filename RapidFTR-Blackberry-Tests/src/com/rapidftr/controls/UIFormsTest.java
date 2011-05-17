package com.rapidftr.controls;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.rapidftr.form.Forms;
import com.rapidftr.model.ChildFactory;

public class UIFormsTest {
	private UIForms uiForms;

	@Before
	public void setUp() throws JSONException {
		uiForms = new UIForms(new Forms(new JSONArray(forms())), Mockito
				.mock(FormFieldFactory.class), ChildFactory.newChild());
	}

	@Test
	public void getFormsNames() throws Exception {
		Object[] formNames = uiForms.getFormNames();
		assertArrayEquals(new String[] { "Basic Details", "Family Details" },
				formNames);
	}

	@Test
	public void getDefaultForms() throws Exception {
		UIForm defaultForm = uiForms.getDefaultForm();
		assertEquals("Basic Details", defaultForm.getName());
	}

	@Test
	public void getIndexByName() throws Exception {
		assertEquals(1, uiForms.getIndexByName("Family Details"));
	}

	@Test
	public void getInvalidNameReturnsZero() throws Exception {
		assertEquals(0, uiForms.getIndexByName("Details"));
	}
	
	private String forms() {
		return "[{'name':'Basic Details','unique_id':'basic_details', 'enabled':true},{'name':'Family Details','unique_id':'family_details', 'enabled':true}]";
	}

}
