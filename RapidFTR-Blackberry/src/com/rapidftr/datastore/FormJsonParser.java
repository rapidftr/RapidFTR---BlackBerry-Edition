package com.rapidftr.datastore;

import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.model.FormFieldFactory;
import net.rim.device.api.ui.component.Dialog;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import java.util.Vector;

public class FormJsonParser {

    FormFieldFactory formFieldFactory = new FormFieldFactory();

    public Vector parse(String json) throws JSONException {
        Vector forms = new Vector();
        try {
            forms = GetForms(new JSONArray(json));
        }
        catch (Error e) {
            Dialog.alert(e.getMessage()); // TODO: Remove direct dependancy on RIM UI.
            e.printStackTrace();
        }
        return forms;
    }

    private Vector GetForms(JSONArray jsonForms) throws JSONException {
        Vector forms = new Vector();
        for (int i = 0; i < jsonForms.length(); i++) {
            JSONObject jsonForm = (JSONObject) jsonForms.get(i);

            forms.addElement(CreateForm(jsonForm));
        }
        return forms;
    }

    private Form CreateForm(JSONObject jsonForm) throws JSONException {
        return new Form(
            jsonForm.getString("name"),
            jsonForm.getString("unique_id"),
            CreateFormFields(jsonForm));
    }

    private Vector CreateFormFields(JSONObject jsonForm) throws JSONException {
        Vector formFields = new Vector();
        JSONArray jsonFormFields = jsonForm.getJSONArray("fields");
        for (int j = 0; j < jsonFormFields.length(); j++) {
            JSONObject jsonFormField = jsonFormFields.getJSONObject(j);
            try {
                if (IsEnabled(jsonFormField)) {
//                    formFields.addElement(CreateFormField(jsonFormField));
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return formFields;
    }

//    private FormField CreateFormField(JSONObject jsonFormField) throws JSONException {
//        String name = jsonFormField.getString("name");
//        String displayName = jsonFormField.getString("display_name");
//        String type = jsonFormField.getString("type");
//        String help_text = GetFormFieldHelpText(jsonFormField);
//        Vector optionStrings = CreateOptions(jsonFormField);
//        return formFieldFactory.createFormField(name, displayName, type, optionStrings, help_text);
//    }

    private String GetFormFieldHelpText(JSONObject jsonFormField) throws JSONException {
        if(jsonFormField.has("help_text"))
        {
            return jsonFormField.getString("help_text");
        }
        return "";
    }

    private Vector CreateOptions(JSONObject jsonFormField) throws JSONException {
        Vector optionStrings = new Vector();
        if(jsonFormField.has("option_strings"))
        {
            JSONArray jsonOptionString = jsonFormField.getJSONArray("option_strings");
            for (int i = 0; i < jsonOptionString.length(); i++) {
                optionStrings.addElement(jsonOptionString.getString(i));
            }
        }
        return optionStrings;
    }

    private boolean IsEnabled(JSONObject jsonFormField) throws JSONException {
        return "true".equals(jsonFormField.getString("enabled"));
    }
}

