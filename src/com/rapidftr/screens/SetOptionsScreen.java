package com.rapidftr.screens;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.utilities.Styles;

public class SetOptionsScreen extends DisplayPage {
	public static final int CLOSE_ACTION = 1;
	
	private LayoutManager layoutManager;
	
	public void initializePage(Object userInfo) {
		String id = (String) userInfo;

		add(new HeaderLayoutManager("3. Other Details", id));
		add(new SeparatorField());

		layoutManager = new LayoutManager();

		add(layoutManager);
	}

	private MenuItem _cancel = new MenuItem("Cancel", 110, 10) {
		public void run() {
			onClose();
		}
	};

	private MenuItem _save = new MenuItem("Save", 110, 10) {
		public void run() {
			onSave();
		}
	};

	protected void makeMenu(Menu menu, int instance) {
		menu.add(_cancel);
		menu.add(_save);
	}
	
	public boolean onSave() {
		Dialog.alert("saved other data");
		
		return true;
	}
	
	public boolean onClose() {
		popScreen(CLOSE_ACTION, null);

		return true;
	}
	
	private class LayoutManager extends Manager {
		private Font defaultFont;
		
		LabelField familyData; 
		
		
		public LayoutManager() {
			super(0);

			defaultFont = Styles.getDefaultFont();

			familyData = new LabelField("Other Data");
			
			add(familyData);
		}
		
		protected void sublayout(int width, int height) {
			layoutChild(familyData, width, 30);
			
			setPositionChild(familyData, 10, 0);
			
			setExtent(width, 30);
		}
	}		
}
