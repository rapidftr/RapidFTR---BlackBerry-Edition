package com.rapidftr.datastore;

import static org.junit.Assert.*;

import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import org.json.me.JSONException;
import org.junit.Test;

import java.util.Vector;

public class FormJsonParserTest {

    @Test
    public void ShouldBeAbleToGetAFormForEachFormDefinedInTheJson() throws JSONException {
        FormJsonParser parser = new FormJsonParser();
        Vector forms = parser.parse(getJson());
        assertEquals(1, forms.size());
        assertEquals("form_name", ((Form)forms.get(0)).getId());
    }

    @Test
    public void ShouldBeAbleToGetTheNameOfAFieldInAForm() throws JSONException {
        FormJsonParser parser = new FormJsonParser();
        Vector forms = parser.parse(getJson());
        Form form = (Form)forms.get(0);
        FormField field = (FormField)form.getFieldList().get(0);
        assertEquals("name", field.getName());
    }

    @Test
    public void ShouldBeAbleToGetTheHelpTextForAFieldInAForm() throws JSONException {
        FormJsonParser parser = new FormJsonParser();
        Vector forms = parser.parse(getJson());
        Form form = (Form)forms.get(0);
        FormField field = (FormField)form.getFieldList().get(0);
        assertEquals("help text", field.getHelpText());
    }

    @Test
    public void ShouldBeAbleToGetAFieldWhenNoHelpIsAvailable() throws JSONException {
        FormJsonParser parser = new FormJsonParser();
        Vector forms = parser.parse(getJsonWithoutHelpText());
        Form form = (Form)forms.get(0);
        FormField field = (FormField)form.getFieldList().get(0);
        assertEquals("", field.getHelpText());
    }

    private String getJson() {
        StringBuilder json = new StringBuilder();
        json.append("[\r\n");
        json.append("   {\r\n");
        json.append("       name: \"formname\",\r\n");
        json.append("       unique_id: \"form_name\",\r\n");
        json.append("       fields: [\r\n");
        json.append("           {\r\n");
        json.append("               name: \"name\",\r\n");
        json.append("               enabled: true,\r\n");
        json.append("               type: \"text_field\",\r\n");
        json.append("               display_name: \"Name\",\r\n");
        json.append("               help_text: \"help text\"\r\n");
        json.append("           }\r\n");
        json.append("       ]\r\n");
        json.append("   }\r\n");
        json.append("]\r\n");
        return json.toString();
    }

        private String getJsonWithoutHelpText() {
        StringBuilder json = new StringBuilder();
        json.append("[\r\n");
        json.append("   {\r\n");
        json.append("       name: \"formname\",\r\n");
        json.append("       unique_id: \"form_name\",\r\n");
        json.append("       fields: [\r\n");
        json.append("           {\r\n");
        json.append("               name: \"name\",\r\n");
        json.append("               enabled: true,\r\n");
        json.append("               type: \"text_field\",\r\n");
        json.append("               display_name: \"Name\"\r\n");
        json.append("           }\r\n");
        json.append("       ]\r\n");
        json.append("   }\r\n");
        json.append("]\r\n");
        return json.toString();
    }
}
