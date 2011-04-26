package com.rapidftr.model;

import com.rapidftr.screens.internal.CustomScreen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Vector;

import static org.mockito.Mockito.*;

/**
 * Created by IntelliJ IDEA.
 * User: balajin
 * Date: 12/17/10
 * Time: 9:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class TabTest {

    @Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
    }

    // Cannot run this test since Mockito doesn't support mocking static methods
/*
    @Test
    public void CreateNewTabView() {
        FormField field = new FormFieldFactory().createFormField("name", "displayname", FormFieldFactory.TEXT_FIELD, null);
        Vector fieldList = new Vector();
        fieldList.add(field);
        Form form = new Form("Test", "id", fieldList);

        String value="abd";
        Child child = new Child();
        child.setField("name", value);


        CustomScreen customScreen = mock(CustomScreen.class);

        Font b = mock(Font.class);
        Tab tabView = new Tab(form, child);
        tabView.RenderOn(customScreen);
        verify(customScreen).add(null);
    }
*/

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
        tabView.RenderOn(customScreen);
        verify(customScreen, never()).add(null);
    }
}
