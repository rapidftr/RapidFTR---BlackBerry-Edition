package com.rapidftr.screens;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
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
import net.rim.device.api.ui.component.Menu;

import com.rapidftr.controllers.SnapshotController;
import com.rapidftr.screens.internal.CustomScreen;

public class SnapshotScreen extends CustomScreen {

	private boolean cameraHasBeenInvoked;

	private Bitmap bitmap;
	private EncodedImage encodedImage;
	private FileSystemJournalListener listener;

	public SnapshotScreen() {
		super();
		listener = new PhotoSaveListener() {

			void photo(String path) {
				try {
					((SnapshotController) controller).capturedImage(path,
							getEncodedImage(path));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	protected void onExposed() {
		controller.popScreen();
		cleanUp();
		super.onExposed();
	}

	protected void onUiEngineAttached(boolean attached) {
		if (!attached)
			return;
		Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments());
		super.onUiEngineAttached(attached);
	}

	public void cleanUp() {
		UiApplication.getUiApplication().removeFileSystemJournalListener(
				listener);
	}

	public void setUp() {
		UiApplication.getUiApplication().addFileSystemJournalListener(listener);
	}

	private EncodedImage getEncodedImage(String path) throws IOException {
		FileConnection fconn = (FileConnection) Connector
				.open("file://" + path);

		InputStream input = fconn.openInputStream();
		byte[] data = new byte[(int) fconn.fileSize()];
		input.read(data, 0, data.length);

		return EncodedImage.createEncodedImage(data, 0, data.length);

	}

	abstract class PhotoSaveListener implements FileSystemJournalListener {
		private long lastUSN;

		public PhotoSaveListener() {
			lastUSN = FileSystemJournal.getNextUSN();
		}

		public void fileJournalChanged() {

			long USN = FileSystemJournal.getNextUSN();

			for (long i = USN - 1; i >= lastUSN; --i) {

				FileSystemJournalEntry entry = FileSystemJournal.getEntry(i);

				if (entry != null) {

					if (entry.getEvent() == FileSystemJournalEntry.FILE_ADDED
							|| entry.getEvent() == FileSystemJournalEntry.FILE_CHANGED
							|| entry.getEvent() == FileSystemJournalEntry.FILE_RENAMED) {

						if (entry.getPath().indexOf(".jpg") != -1) {
							photo(entry.getPath());
							injectKey(Characters.ESCAPE);
							injectKey(Characters.ESCAPE);
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

		abstract void photo(String path);
	}
}
