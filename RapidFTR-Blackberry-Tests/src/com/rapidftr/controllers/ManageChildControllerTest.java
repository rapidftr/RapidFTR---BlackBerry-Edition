package com.rapidftr.controllers;


import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.me.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.rapidftr.Key;
import com.rapidftr.controllers.internal.Dispatcher;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.datastore.MockStore;
import com.rapidftr.form.Forms;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildFactory;
import com.rapidftr.screens.ManageChildScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.utilities.CustomDialog;
import com.rapidftr.utilities.DateFormatter;

public class ManageChildControllerTest {

    private static final String CURRENT_DATE_TIME = "2010-11-2 01:00:00GMT";
	private ManageChildScreen screen;
    private UiStack uiStack;
    private FormStore formStore;
    private ChildrenRecordStore childRecordStore;
    private Dispatcher dispatcher;
    private ManageChildController manageChildController;
    private DateFormatter dateFormatter;
    private Forms forms;
    private CustomDialog customDialog;

    @Before
    public void setup() throws Exception {
        screen = mock(ManageChildScreen.class);
        dispatcher = mock(Dispatcher.class);
        uiStack = mock(UiStack.class);
        childRecordStore = new ChildrenRecordStore(new MockStore(new Key("childrenrecord")));
        formStore = new FormStore(new MockStore(new Key("forms")));
        manageChildController = new ManageChildController(screen, uiStack, formStore, childRecordStore, dispatcher);
        
        dateFormatter = mock(DateFormatter.class);
        when(dateFormatter.getCurrentFormattedDateTime()).thenReturn(CURRENT_DATE_TIME);
        forms = new Forms(new JSONArray(twoForms()));
        customDialog = mock(CustomDialog.class);
        manageChildController.setCustomDialog(customDialog);
    }
    
    @Test
    public void shouldCreateAndSaveANewChild(){
    	Mockito.doNothing().when(customDialog).alert(anyString());
    	assertChild();
    }

	private void assertChild() {
		manageChildController.validateOnSave(forms, dateFormatter);
    	assertEquals(1,childRecordStore.getAll().count());
    	Child actualChild = childRecordStore.getChildAt(0);
    	assertEquals("New",actualChild.childStatus().getStatusString());
    	assertEquals(CURRENT_DATE_TIME,actualChild.getField("created_at"));
    	verify(customDialog, times(0)).alert(anyString());
	}
    
    @Test
    public void shouldUpdateAnExistingChild(){
    	Mockito.doNothing().when(customDialog).alert(anyString());
    	manageChildController.setChild(ChildFactory.existingChild("id"));
    	assertChild();
    	Child actualChild = childRecordStore.getChildAt(0);
    	assertEquals("id", actualChild.getField("_id"));
    	assertEquals("uniqueid", actualChild.getField("unique_identifier"));
    }
    
	private String twoForms() {
		return "[{'name':'Basic Details','unique_id':'basic_details', 'enabled':true},{'name':'Family Details','unique_id':'family_details', 'enabled':true}]";
	}

}
