package com.rapidftr.screens;

import com.rapidftr.controllers.ChildController;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.BoldRichTextField;
import com.rapidftr.utilities.ChildFieldIgnoreList;
import com.rapidftr.utilities.ImageUtility;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.*;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import javax.microedition.io.Connector;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

public class ViewChildScreen extends CustomScreen {

	Child child;

	public ViewChildScreen() {
	}

	public void setChild(Child child) {
		this.child = child;
		clearFields();
		add(new LabelField("Child Details"));
		add(new SeparatorField());
		renderChildFields(child);

	}

	private void renderChildFields(Child child) {
		int index = 0;

		// render the picture
		HorizontalFieldManager hmanager = new HorizontalFieldManager(
				Manager.HORIZONTAL_SCROLLBAR);
		renderBitmap(hmanager, (String) child.getField("current_photo_key"));

		hmanager.add(new BoldRichTextField("   " + child.getField("name")));
		add(hmanager);
		// add an empty line
		add(new LabelField(""));

		// render the unique id
		String uniqueIdentifier = (String) child.getField("unique_identifier");
		uniqueIdentifier = (null == uniqueIdentifier) ? "" : uniqueIdentifier;

		add(BoldRichTextField.getSemiBoldRichTextField("Unique Id" + " :",
				uniqueIdentifier));
		add(new SeparatorField());

		// render other fields
		Vector forms = new FormStore().getForms();

		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			Form form = (Form) list.nextElement();
			for (Enumeration fields = form.getFieldList().elements(); fields
					.hasMoreElements();) {
				Object nextElement = fields.nextElement();

				if (nextElement != null) {
					FormField field = (FormField) nextElement;
					String key = field.getName();
					child.updateField(key);
					String value = (String) child.getField(key);
					if (ChildFieldIgnoreList.isInIgnoreList(key)) {
						continue;
					}

					key = key.replace('_', ' ');

					add(BoldRichTextField.getSemiBoldRichTextField(field.displayLabel() + " :", value));
					add(new SeparatorField());
					index++;
				}
			}

		}

	}

	private void renderBitmap(HorizontalFieldManager manager, String currentPhotoKey) {
		manager.setMargin(10, 10, 10, 10);

		Bitmap image = getChildImage(currentPhotoKey);

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
				// Move from edit screen directly to the main menu application
				// screen
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
		if (child.isSyncFailed()) {
			MenuItem syncMenu = new MenuItem("Sync Errors", 2, 1) {
				public void run() {
					UiApplication.getUiApplication().invokeLater(
							new Runnable() {
								public void run() {
									Dialog.alert(child.childStatus()
											.getSyncError());

								}
							});
				}
			};
			menu.add(syncMenu);
		}
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
			EncodedImage eimg = EncodedImage.createEncodedImage(data, 0,
					data.length);
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
