package com.rapidftr.controls;

import org.junit.Before;
import org.junit.Ignore;
import org.mockito.MockitoAnnotations;

public class TabTest {

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Ignore
	public void ShouldNotRenderFiledsWhichAReInIgnoreList() {
		//TODO: fix this
		/*FormField field = new FormFieldFactory().createFormField("histories",
				"displayname", FormFieldFactory.TEXT_FIELD, null, "");
		Vector<FormField> fieldList = new Vector<FormField>();
		fieldList.add(field);
		Form form = new Form("Test", "id", fieldList);

		String value = "abd";
		Child child = ChildFactory.newChild();
		child.setField("histories", value);

		CustomScreen customScreen = mock(CustomScreen.class);

		Tab tabView = new Tab(form.toString(), form, child);
		// tabView.render();
		verify(customScreen, never()).add(null);*/
	}
}
