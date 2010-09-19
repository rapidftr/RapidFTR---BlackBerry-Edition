package com.rapidftr.screens;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.controls.TitleField;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.BoldRichTextField;
import com.rapidftr.utilities.ChildFieldIgnoreList;
import com.rapidftr.utilities.ImageUtility;

public class ViewChildScreen extends CustomScreen {

	Child child;

	public ViewChildScreen() {
	}

	public void setChild(Child child) {
		this.child = child;
		clearFields();
		add(new TitleField());
		add(new LabelField("Child Details"));
		add(new SeparatorField());
		renderChildFields(child);

	}

	private void renderChildFields(Child child) {
		//updateChildFieldsWithLatestForms(child);
		Hashtable data = child.getKeyMap();
		HorizontalFieldManager hmanager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLLBAR);
		renderBitmap(data, hmanager);

		hmanager.add(new BoldRichTextField("   " + data.get(new String("name"))));
		add(hmanager);
		//RichTextField richField[] = new RichTextField[data.size()];
		int index = 0;

		Vector forms = new FormStore().getForms();

		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			Form form = (Form) list.nextElement();
			for (Enumeration fields = form.getFieldList().elements(); fields.hasMoreElements();) {
				Object nextElement = fields.nextElement();

				if (nextElement != null) {
					FormField field = (FormField) nextElement;
					String key = field.getName();
					child.updateField(key);
					String value = (String) data.get(key);
					if (ChildFieldIgnoreList.isInIgnoreList(key)) {
						continue;
					}

					key = key.replace('_', ' ');
					//richField[index] = BoldRichTextField.getSemiBoldRichTextField(key + " :", value);

					add(BoldRichTextField.getSemiBoldRichTextField(key + " :", value));
					add(new SeparatorField());
					index++;
				}
			}

		}

	}


	private void renderBitmap(Hashtable data, HorizontalFieldManager manager) {
		manager.setMargin(10, 10, 10, 10);

		Bitmap image = getChildImage((String) data.get("current_photo_key"));

		if (image == null)
			image = getChildImage("res/default.jpg");

		if (image != null) {
			BitmapField bf = new BitmapField(image, BitmapField.FOCUSABLE);
			manager.add(bf);
		}
	}

	public void setUp() {
		// TODO Auto-generated method stub
	}

	public void cleanUp() {

	}

	protected void makeMenu(Menu menu, int instance) {
		MenuItem editChildMenu = new MenuItem("Edit Child Detail", 1, 1) {
			public void run() {
				// Move from edit screen directly to the main menu application screen
				controller.popScreen();
				((ChildController) controller).editChild(child);
			}
		};

		MenuItem historyMenu = new MenuItem("View The Change Log", 2, 1) {
			public void run() {
				((ChildController) controller).showHistory(child);
			}
		};

		MenuItem CloseMenu = new MenuItem("Close", 2, 1) {
			public void run() {
				controller.popScreen();
			}
		};

		menu.add(editChildMenu);
		menu.add(historyMenu);
		menu.add(CloseMenu);
	}

	private Bitmap getChildImage(String ImagePath) {
		try {
			InputStream inputStream = Connector.openInputStream(ImagePath);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int i = 0;
			while ((i = inputStream.read()) != -1)
				outputStream.write(i);

			byte[] data = outputStream.toByteArray();
			EncodedImage eimg = EncodedImage.createEncodedImage(data, 0, data.length);
			Bitmap image = eimg.getBitmap();
			inputStream.close();

			return ImageUtility.resizeBitmap(image, 70, 70);
		} catch (IOException e) {
			return null;
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
}
