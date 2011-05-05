package com.rapidftr.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.Key;
import com.rapidftr.form.Forms;
import com.sun.me.web.path.ResultException;

public class FormStoreTest {

	private MockStore mockStore;
	private FormStore store;

	@Before
	public void setUp() {
		mockStore = new MockStore(new Key("forms"));
		store = new FormStore(mockStore);
	}

	@SuppressWarnings("serial")
	@Test
	public void storeForms() throws ResultException {
		store.storeForms("[{form:forms}]");
		assertEquals("[{form:forms}]", mockStore.getString("forms"));

	}

	@Test
	public void clearState() throws Exception {
		store.storeForms("[{form:forms}]");
		store.clearState();
		assertEquals("", mockStore.getString("forms"));
	}

	@Test
	public void getEmptyFormsForMalFormedJSONString() throws Exception {
		store.storeForms("{");
		Forms forms = store.getForms();
		assertTrue(forms.isEmpty());
	}

	@Test
	public void getForms() throws Exception {
		store
				.storeForms("[{'name':'Basic Details','unique_id':'basic_details', 'enabled':true , " +
						"fields: " +
						"[{'name':'name'," +
						"'enabled':true," +
						"'type':'text_field'," +
						"'display_name':'Name'}]}]");
		Forms forms = store.getForms();
		assertTrue(forms.isNotEmpty());
	}
}
