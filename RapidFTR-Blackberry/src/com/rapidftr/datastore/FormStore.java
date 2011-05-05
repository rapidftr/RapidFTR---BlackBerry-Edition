package com.rapidftr.datastore;

import org.json.me.JSONArray;
import org.json.me.JSONException;

import com.rapidftr.form.Forms;

public class FormStore {

    static final long KEY = 0x6699d842f70a9c2cL; // com.rapidftr.datastore.FormStore
    protected PersistentStore persistentStore;

    public FormStore() {

        initializePersistentStore();
    }

    void initializePersistentStore() {
        persistentStore = new PersistentStore(KEY);
    }
    
    public void storeForms(String forms) throws JSONException {
        persistentStore.setContents(forms);
    }

    public void clearState() {
        persistentStore.setContents("");
    }

	public Forms getHigherForms() {
		try{
			return new Forms(new JSONArray((String) persistentStore.getContents()));
		}catch(Exception e){
			return new Forms();
		}
	}

}