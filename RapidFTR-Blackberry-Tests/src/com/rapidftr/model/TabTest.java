package com.rapidftr.model;

import com.rapidftr.screens.internal.CustomScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Vector;

import static org.mockito.Mockito.*;

public class TabTest {

    @Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
    }


    @Test
    public void ShouldNotRenderFiledsWhichAReInIgnoreList() {
        FormField field = new FormFieldFactory().createFormField("histories", "displayname", FormFieldFactory.TEXT_FIELD, null, "");
        Vector fieldList = new Vector();
        fieldList.add(field);
        Form form = new Form("Test", "id", fieldList);

        String value="abd";
        Child child = ChildFactory.newChild();
        child.setField("histories", value);


        CustomScreen customScreen = mock(CustomScreen.class);

        Tab tabView = new Tab(form.toString(),form, child);
        tabView.render(customScreen);
        verify(customScreen, never()).add(null);
    }
}
