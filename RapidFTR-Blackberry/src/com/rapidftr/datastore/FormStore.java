package com.rapidftr.datastore;

import com.rapidftr.model.Form;
import com.rapidftr.model.FormFieldFactory;
import net.rim.device.api.ui.component.Dialog;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.util.Vector;

public class FormStore {

    static final long KEY = 0x6699d842f70a9c2cL; // com.rapidftr.datastore.FormStore
    protected PersistentStore persistentStore;

    public FormStore() {
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
            return parseFormsFromJSON(jsonString);
        } catch (JSONException e) {
            return new Vector();
        }
    }

    private Vector parseFormsFromJSON(String json) throws JSONException {
        Vector forms = new Vector();
        try {
            FormFieldFactory formFieldFactory = new FormFieldFactory();
            JSONArray jsonForms = new JSONArray(json);

            for (int i = 0; i < jsonForms.length(); i++) {
                JSONObject jsonForm = (JSONObject) jsonForms.get(i);
                Vector formFields = new Vector();
                JSONArray jsonFormFields = jsonForm.getJSONArray("fields");
                for (int j = 0; j < jsonFormFields.length(); j++) {
                    JSONObject jsonFormField = jsonFormFields.getJSONObject(j);
                    Vector optionStrings = new Vector();
                    try {
                        String name = jsonFormField.getString("name");
                        String displayName = jsonFormField.getString("display_name");
                        String type = jsonFormField.getString("type");
                        try {
                            JSONArray jsonOptionString = jsonFormField
                                    .getJSONArray("option_strings");
                            for (int k = 0; k < jsonOptionString.length(); k++) {
                                optionStrings.addElement(jsonOptionString
                                        .getString(k));
                            }
                        } catch (JSONException ex) {
                            //The JSON array does not contains "option_strings"
                            //ex.printStackTrace();
                        }

                        formFields.addElement(formFieldFactory.createFormField(name, displayName, type, optionStrings));
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                Form form = new Form(jsonForm.getString("name"), jsonForm
                        .getString("unique_id"), formFields);
                forms.addElement(form);

            }
        }
        catch (Error e) {
            Dialog.alert(e.getMessage());
            e.printStackTrace();
        }
        return forms;
    }

    public void storeForms(String forms) throws JSONException {
        parseFormsFromJSON(forms);
        persistentStore.setContents(forms);
    }

    public void clearState() {
        persistentStore.setContents("");
    }

}