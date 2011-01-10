package com.rapidftr.screens;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.io.Connector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FocusChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.decor.Border;
import net.rim.device.api.ui.decor.BorderFactory;

import com.rapidftr.controllers.ViewChildController;
import com.rapidftr.datastore.FormStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.ChildStatus;
import com.rapidftr.model.Form;
import com.rapidftr.model.Tab;
import com.rapidftr.model.TabControl;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.BoldRichTextField;
import com.rapidftr.utilities.ImageUtility;

public class ViewChildScreen extends CustomScreen {

	Child child;
	BitmapField bitmapField;
	boolean isBitmapFieldFocused = false;

	public ViewChildScreen() {
	}

	public void setChild(Child child) {
		this.child = child;
	}

	public void setUp() {
		clearFields();
		HorizontalFieldManager titleManager = new HorizontalFieldManager(
				USE_ALL_WIDTH);
		titleManager.setPadding(new XYEdges(2, 2, 2, 0));
		titleManager.add(new LabelField("Child Details"));
		VerticalFieldManager titleSyncStatusManager = new VerticalFieldManager(
				USE_ALL_WIDTH);
		ChildStatus childSyncStatus = child.childStatus();
		String syncMessage = child.childStatus().getStatusString();
		LabelField syncStatus = new LabelField(syncMessage, FIELD_RIGHT) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			}
		};
		XYEdges labelEdges = new XYEdges(1, 1, 1, 1);
		syncStatus.setBorder(BorderFactory.createBevelBorder(labelEdges));
		syncStatus.setBackground(BackgroundFactory
				.createSolidBackground(childSyncStatus.getStatusColor()));
		titleSyncStatusManager.add(syncStatus);
		titleManager.add(titleSyncStatusManager);
		this.add(titleManager);
		this.add(new SeparatorField());
		renderChildFields(child);
	}

	protected void onExposed() {
		setUp();
	}
	
	private void renderChildFields(final Child child) {
		
		final HorizontalFieldManager horizontalFieldManager = new HorizontalFieldManager(
				Manager.HORIZONTAL_SCROLLBAR | Manager.USE_ALL_WIDTH);		
		
		renderBitmap(horizontalFieldManager, (String) child
				.getField("current_photo_key"));
		
		String uniqueIdentifier = (String) child.getField("unique_identifier");
		uniqueIdentifier = (null == uniqueIdentifier) ? "" : uniqueIdentifier;
		horizontalFieldManager.add(BoldRichTextField.getSemiBoldRichTextField(" ",uniqueIdentifier));
		
		add(horizontalFieldManager);


		LabelField emptyLineAfterUID = new LabelField("");
		emptyLineAfterUID.select(false);
		add(emptyLineAfterUID);

		renderFormFields(child);
	}

	private void renderFormFields(Child child) {
		
		Vector forms = new FormStore().getForms();
		
		Tab[] tabList = new Tab[forms.size()];
		
		int i = 0;

		for (Enumeration list = forms.elements(); list.hasMoreElements();) {
			Form form = (Form) list.nextElement();
			tabList[i++] = new Tab(form.toString(), form, child);
		}
		
		TabControl tabView = new TabControl(tabList);
		
		this.add(tabView);
	}

	private void renderBitmap(HorizontalFieldManager manager,
			String currentPhotoKey) {
		manager.setMargin(10,10,10,10);

		Bitmap image = getChildImage(currentPhotoKey);

		if (image == null) {
			image = getChildImage("res/default.jpg");
		}

		final BitmapField bitmapField = new BitmapField(image,
				BitmapField.FOCUSABLE);
		bitmapField.select(true);

		bitmapField.setFocusListener(new FocusChangeListener() {
			public void focusChanged(Field field, int eventType) {
				if (eventType == FOCUS_GAINED) {
					isBitmapFieldFocused = true;
					Border blueBorder = BorderFactory.createSimpleBorder(
							new XYEdges(2, 2, 2, 2),
							new XYEdges(Color.BLUE,
									Color.BLUE,
									Color.BLUE,
									Color.BLUE),
							Border.STYLE_SOLID);
					bitmapField.setBorder(blueBorder);
				} else if (eventType == FOCUS_LOST) {
					isBitmapFieldFocused = false;
					bitmapField.setBorder(null);

				}
			}
		});

		manager.add(bitmapField);
	}

	protected boolean trackwheelClick(int status, int time) {
		if (isBitmapFieldFocused) {
			((ViewChildController) controller).viewChildPhoto(child);
			return false;

		}
		return true;
	}

	private ViewChildController getViewChildController() {
		return ((ViewChildController) controller);
	}
	
	protected void makeMenu(Menu menu, int instance) {
		MenuItem editChildMenu = new MenuItem("Edit Child Detail", 1, 1) {
			public void run() {
				controller.popScreen();
				getViewChildController().editChild(child);
			}
		};

		MenuItem photoMenu = new MenuItem("View Child Photo", 2, 1) {
			public void run() {
				getViewChildController().viewChildPhoto(child);
			}
		};

		MenuItem historyMenu = new MenuItem("View The Change Log", 2, 1) {
			public void run() {
				getViewChildController().showHistory(child);
			}
		};

		MenuItem syncChildMenu = new MenuItem("Synchronise this Record", 2, 1) {
            public void run() {
            	getViewChildController().syncChild(child);
            }
        };

		menu.add(editChildMenu);
		menu.add(photoMenu);
		menu.add(syncChildMenu);

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

		super.makeMenu(menu, instance);
	}

	private Bitmap getChildImage(String ImagePath) {
		try {
			InputStream inputStream = Connector.openInputStream(ImagePath);

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int i = 0;
			while ((i = inputStream.read()) != -1) {
                outputStream.write(i);
            }

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
