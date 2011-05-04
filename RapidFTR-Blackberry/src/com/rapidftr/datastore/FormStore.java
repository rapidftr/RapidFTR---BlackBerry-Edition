package com.rapidftr.datastore;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;

import com.rapidftr.form.Forms;
import com.rapidftr.model.OldForms;

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

    private Vector getForms() {
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
		//TODO: remove this
		//return new OldForms(getForms());
		try{
			return new Forms(new JSONArray((String) persistentStore.getContents()));
		}catch(Exception e){
			return null;
		}
	}

	public OldForms getOldForms() {
		return new OldForms(getForms());
	}

}