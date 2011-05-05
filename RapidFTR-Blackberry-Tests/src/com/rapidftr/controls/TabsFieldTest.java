package com.rapidftr.controls;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.junit.Test;

import com.rapidftr.form.Form;

public class TabsFieldTest {

	@Test
	public void openDefaultTabOnDraw() {
		TabsField tabsField = createTabsField();
		Tab tab1 = createTab("tab1");
		Tab tab2 = createTab("tab2");

		tabsField.addTab(tab1);
		tabsField.addTab(tab2);
		tabsField.draw();

		verify(tab1).open();
		verify(tab2, never()).open();
	}

	@Test
	public void toggleSelectedTab() {
		TabsField tabsField = createTabsField();
		Tab tab1 = createTab("tab1");
		Tab tab2 = createTab("tab2");

		tabsField.addTab(tab1);
		tabsField.addTab(tab2);
		tabsField.draw();

		tab2.focusChanged(mock(TabLabelField.class), 1);

		verify(tab1).close();
		verify(tab2).open();
	}

	@Test
	public void getSelectedTabName() throws Exception {
		TabsField tabsField = createTabsField();
		Tab tab1 = createTab("tab1");
		Tab tab2 = createTab("tab2");

		tabsField.addTab(tab1);
		tabsField.addTab(tab2);
		tabsField.draw();
		
		assertEquals("tab1", tabsField.getSelectedTab());
		tab2.focusChanged(mock(TabLabelField.class), 1);
		assertEquals("tab2", tabsField.getSelectedTab());


	}

	private Tab createTab(String label) {
		TabLabelField mockLabelField = mock(TabLabelField.class);
		when(mockLabelField.getText()).thenReturn(label);
		return spy(new Tab(mock(Form.class), null, mockLabelField,
				mock(TabBodyField.class)));
		
	}

	private TabsField createTabsField() {
		return new TabsField(mock(VerticalFieldManager.class),
				mock(HorizontalFieldManager.class),
				mock(VerticalFieldManager.class));
	}
}
