package com.rapidftr.screens;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.CheckboxField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;

import com.rapidftr.layouts.HeaderLayoutManager;
import com.rapidftr.model.Options;
import com.rapidftr.model.ProtectionConcerns;
import com.rapidftr.utilities.Styles;

public class SetOptionsScreen extends DisplayPage {
	private LayoutManager layoutManager;
	
	public void initializePage(Object userInfo) {
		String id = (String) userInfo;

		add(new HeaderLayoutManager("4. Options", id));
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
		Options options = new Options();

		for (int i = 0; i < Options.NAMES.length; i++) {
			CheckboxField field = layoutManager.items[i];

			options.setOption(i, field.getChecked());
		}

		//popScreen(POP_ACTION, options);
		
		return true;
	}
	
	public boolean onClose() {
		//popScreen(POP_ACTION, null);

		return true;
	}
	
	private class LayoutManager extends Manager {
		private Font defaultFont;
		
		LabelField header; 
		CheckboxField items[];
		
		public LayoutManager() {
			super(0);

			defaultFont = Styles.getDefaultFont();

			header = new LabelField("Options");
			header.setFont(Styles.getHeaderFont());
			
			add(header);
			
			items = new CheckboxField[Options.NAMES.length];

			for (int i = 0; i < Options.NAMES.length; i++) {
				items[i] = new CheckboxField(Options.NAMES[i], false);
				items[i].setFont(defaultFont);
				add(items[i]);
			}
		}
		
		protected void sublayout(int width, int height) {
			layoutChild(header, width, 30);
			
			setPositionChild(header, 10, 0);
			
			for (int i = 0; i < items.length; i++) {
				layoutChild(items[i], width, 30);
				setPositionChild(items[i], 10, 20 + (20 * i));
			}

			setExtent(width, 30 + (items.length * 20));
		}
	}		
}
