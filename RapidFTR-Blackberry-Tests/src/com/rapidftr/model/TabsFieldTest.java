package com.rapidftr.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;

import org.junit.Test;

public class TabsFieldTest {

	@Test
	public void openDefaultTabOnDraw() {
		TabsField tabsField = createTabsField();
		Tab tab1 = createTab();
		Tab tab2 = createTab();

		tabsField.addTab(tab1);
		tabsField.addTab(tab2);
		tabsField.draw();

		verify(tab1).open();
		verify(tab2, never()).open();
	}

	private Tab createTab() {
		return spy(new Tab(mock(Form.class), null,
				mock(TabLabelField.class), mock(TabBodyField.class)));
	}

	private TabsField createTabsField() {
		return new TabsField(mock(VerticalFieldManager.class),
				mock(HorizontalFieldManager.class),
				mock(VerticalFieldManager.class));
	}

	@Test
	public void toggleSelectedTab() {
		TabsField tabsField = createTabsField();
		Tab tab1 = createTab();
		Tab tab2 = createTab();

		tabsField.addTab(tab1);
		tabsField.addTab(tab2);
		tabsField.draw();

		tab2.focusChanged(mock(TabLabelField.class), 1);

		verify(tab1).close();
		verify(tab2).open();
	}
}
