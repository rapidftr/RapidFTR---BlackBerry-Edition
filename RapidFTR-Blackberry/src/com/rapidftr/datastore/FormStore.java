package com.rapidftr.datastore;

import java.util.Vector;

import org.json.me.JSONException;

import com.rapidftr.model.Forms;

public class FormStore {

    static final long KEY = 0x6699d842f70a9c2cL; // com.rapidftr.datastore.FormStore
    protected PersistentStore persistentStore;
    private FormJsonParser parser;

    public FormStore(FormJsonParser parser) {

        this.parser = parser;
        initializePersistentStore();
    }

    void initializePersistentStore() {
        persistentStore = new PersistentStore(KEY);
    }

    public Vector getForms() {
        String jsonString = (String) persistentStore.getContents();
        try {
            if (jsonString == null) {
                jsonString = "";
            }
            return parser.parse(jsonString);
        } catch (JSONException e) {
            return new Vector();
        }
    }
    
    public void storeForms(String forms) throws JSONException {
        parser.parse(forms);
        persistentStore.setContents(forms);
    }

    public void clearState() {
        persistentStore.setContents("");
    }

	public Forms getHigherForms() {
		return new Forms(getForms());
	}

}