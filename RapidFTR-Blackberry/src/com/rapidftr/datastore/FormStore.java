package com.rapidftr.datastore;

import org.json.me.JSONArray;

import com.rapidftr.form.Forms;
import com.rapidftr.utilities.Store;

public class FormStore {

	private final Store store;

	public FormStore(Store store) {

		this.store = store;
	}

	public void storeForms(String forms) {
		store.setString("forms", forms);
	}

	public void clearState() {
		store.setString("forms", "");
	}

	public Forms getForms() {
		try {
			return new Forms(new JSONArray(store.getString("forms")));
		} catch (Exception e) {
			return new Forms(new JSONArray());
		}
	}

}