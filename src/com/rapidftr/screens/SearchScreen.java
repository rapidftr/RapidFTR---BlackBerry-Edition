package com.rapidftr.screens;

import java.util.Hashtable;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;

import com.rapidftr.controls.BorderedEditField;
import com.rapidftr.controls.Button;
import com.rapidftr.controls.RecordList;
import com.rapidftr.model.ChildRecord;
import com.rapidftr.model.ChildRecordItem;
import com.rapidftr.services.ServiceException;
import com.rapidftr.services.ServiceManager;
import com.rapidftr.utilities.Styles;

public class SearchScreen extends DisplayPage {

	private static final int RECORD_EDIT_ACTION = 1;
	
	private BorderedEditField searchField;
	private RecordList recordList;
	private ChildRecordItem model[];

	public SearchScreen() {
		LayoutManager layoutManager = new LayoutManager();

		add(layoutManager);

		recordList = layoutManager.recordList;
		searchField = layoutManager.searchField;

		layoutManager.goButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				onGo();
			}
		});
	}

	private MenuItem _go = new MenuItem("Go", 110, 10) {
		public void run() {
			onGo();
		}
	};

	private MenuItem _open = new MenuItem("Open Selected", 110, 10) {
		public void run() {
			onOpen();
		}
	};

	private MenuItem _cancel = new MenuItem("Cancel", 110, 10) {
		public void run() {
			onClose();
		}
	};

	protected void makeMenu(Menu menu, int instance) {
//		menu.add(_go);
		menu.add(_open);
		menu.add(_cancel);
	}

	private void onGo() {
		String searchCriteria = searchField.getText();

		try {
			model = ServiceManager.getRecordService()
					.getMatches(searchCriteria);
		} catch (ServiceException se) {
			Dialog.alert("Service Exception: " + se);
		}

		recordList.setModel(model);

		this.invalidate();
	}

	private void onOpen() {
		int index = recordList.getSelectedIndex();

		ChildRecord record = null;

		try {
			record = ServiceManager.getRecordService().getRecord(model[index]);
		} catch (ServiceException se) {
			Dialog.alert("Service Exception: " + se);
		}

		Hashtable userInfo = new Hashtable();
		userInfo.put("record", record);
		userInfo.put("type", String.valueOf(NavigatorScreen.TYPE_EDIT) );
		
		//pushScreen(RECORD_EDIT_ACTION, userInfo);
	}

	public boolean onClose() {
		//popScreen(POP_ACTION, null);

		return true;
	}

	/**
	 * Layout Manager
	 * 
	 * @author Donal
	 * 
	 */
	private class LayoutManager extends Manager {
		public Button goButton;
		public RecordList recordList;
		public BorderedEditField searchField;

		private LabelField header;
		private LabelField subtext;

		public LayoutManager() {
			super(0);

			header = new LabelField("Search by Name or Registration ID") {
				public void paint(Graphics graphics) {
					graphics.setColor(0x00008800);
					super.paint(graphics);
				}
			};

			header.setFont(Styles.getHeaderFont());

			subtext = new LabelField("(enter the first few letters)");
			subtext.setFont(Styles.getDefaultFont());

			searchField = new BorderedEditField("Name: ", "", 18);

			int limit = 30;

			goButton = new Button("Go", limit);

			recordList = new RecordList();

			add(header);
			add(subtext);
			add(searchField);
			add(goButton);
			add(recordList);
		}

		protected void sublayout(int width, int height) {
			layoutChild(header, width, 25);
			layoutChild(subtext, width, 25);
			layoutChild(searchField, 250, 25);
			layoutChild(goButton, width / 2, 25);
			layoutChild(recordList, width, height);

			setPositionChild(header, (width - header.getWidth()) / 2, 0);
			setPositionChild(subtext, (width - subtext.getWidth()) / 2, 20);

			setPositionChild(searchField, 10, 50);
			setPositionChild(goButton, searchField.getWidth() + 10, 45);

			setPositionChild(recordList, 10, 80);

			setExtent(width, height);
		}
	}
}
