package com.rapidftr.screens;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.math.Fixed32;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.EventInjector.KeyEvent;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.Dialog;

import com.rapidftr.controllers.SnapshotController;

public class SnapshotScreen extends CustomScreen {

	private BitmapField bitmapField;
	private String photoPath;
	private boolean cameraHasBeenInvoked;
	private static long lastUSN;
	private Bitmap bitmap;
	private EncodedImage encodedImage;
	private FileSystemJournalListener listener;

	public SnapshotScreen() {
		super();
		photoPath = null;
		cameraHasBeenInvoked = false;
		bitmapField = new BitmapField();
		add(bitmapField);
		listener = createFileSystemListener();
		addMenuItem(new MenuItem("Save", 1, 1) {
			public void run() {
				onSaveClicked();
			}

		});

	}

	private void onSaveClicked() {
		((SnapshotController) controller)
		.capturedImage(photoPath, encodedImage);
		cleanUp();
		controller.popScreen();
	}

	protected void onExposed() {
		if (photoPath != null && cameraHasBeenInvoked) {
			showImage();
			bitmapField.setBitmap(bitmap);
		}

		if (cameraHasBeenInvoked && photoPath == null) {
			System.out.println("pop screen called");
			cleanUp();
			controller.popScreen();
		}

		super.onExposed();
	}

	protected void onDisplay() {

		if (!cameraHasBeenInvoked) {
			cameraHasBeenInvoked = true;
			Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA,
					new CameraArguments());

		}

		super.onDisplay();
	}

	public void cleanUp() {
		cameraHasBeenInvoked = false;
		photoPath = null;
		UiApplication.getUiApplication().removeFileSystemJournalListener(
				listener);
	}

	public void setUp() {
		UiApplication.getUiApplication().addFileSystemJournalListener(listener);
	}

	private void showImage() {
		try {
			FileConnection fconn = (FileConnection) Connector.open("file://"
					+ photoPath);

			InputStream input = fconn.openInputStream();
			byte[] data = new byte[(int) fconn.fileSize()];
			input.read(data, 0, data.length);
			
			encodedImage = EncodedImage
					.createEncodedImage(data, 0, data.length);

			int currentWidthFixed32 = Fixed32.toFP(encodedImage.getWidth());
			int currentHeightFixed32 = Fixed32.toFP(encodedImage.getHeight());

			int requiredWidthFixed32 = Fixed32.toFP(Display.getWidth());
			int requiredHeightFixed32 = Fixed32.toFP(Display.getHeight() - 35);

			int scaleXFixed32 = Fixed32.div(currentWidthFixed32,
					requiredWidthFixed32);
			int scaleYFixed32 = Fixed32.div(currentHeightFixed32,
					requiredHeightFixed32);

			bitmap = encodedImage.scaleImage32(scaleXFixed32, scaleYFixed32)
					.getBitmap();

		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	protected void makeContextMenu(ContextMenu contextMenu) {
		super.makeContextMenu(contextMenu);
	}

	private FileSystemJournalListener createFileSystemListener() {

		lastUSN = FileSystemJournal.getNextUSN();
		FileSystemJournalListener listener = new FileSystemJournalListener() {
			public void fileJournalChanged() {

				long USN = FileSystemJournal.getNextUSN();

				for (long i = USN - 1; i >= lastUSN; --i) {

					FileSystemJournalEntry entry = FileSystemJournal
							.getEntry(i);

					if (entry != null) {

						if (entry.getEvent() == FileSystemJournalEntry.FILE_ADDED
								|| entry.getEvent() == FileSystemJournalEntry.FILE_CHANGED
								|| entry.getEvent() == FileSystemJournalEntry.FILE_RENAMED) {

							if (entry.getPath().indexOf(".jpg") != -1) {
								photoPath = entry.getPath();
								injectKey(Characters.ESCAPE);
								injectKey(Characters.ESCAPE);
								System.out.println("photo path" + photoPath);

								lastUSN = USN;

							}

						}

					}

				}

			}

			private void injectKey(char key) {
				KeyEvent inject = new KeyEvent(KeyEvent.KEY_DOWN, key, 0);
				inject.post();
			}

		};
		return listener;
	}

	public boolean onClose() {

		if (bitmap != null) {
			int result = Dialog.ask(Dialog.D_SAVE);

			if (result == Dialog.SAVE) {
				onSaveClicked();
				return true;
			}
		}

		cleanUp();

		return super.onClose();

	}

}
