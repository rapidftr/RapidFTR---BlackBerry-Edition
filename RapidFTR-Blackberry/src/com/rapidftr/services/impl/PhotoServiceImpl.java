package com.rapidftr.services.impl;

import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.system.EventInjector;
import net.rim.device.api.ui.UiApplication;

import com.rapidftr.services.PhotoService;
import com.rapidftr.services.PhotoServiceListener;
import com.rapidftr.utilities.Utilities;

public class PhotoServiceImpl implements PhotoService {
	private static final String IMAGE_NAME = "img/cait.jpg";

	private static PhotoService instance;

	private long _lastUSN; // = 0;
	private PhotoServiceListener listener;

	public static synchronized PhotoService getInstance() {
		if (instance == null) {
			instance = new PhotoServiceImpl();
		}

		return instance;
	}

	private PhotoServiceImpl() {
		JournalListener jl = new JournalListener();

		UiApplication.getUiApplication().addFileSystemJournalListener(jl);
	}

	public void startCamera(PhotoServiceListener listener) {
		this.listener = listener;

		Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments());
	}

	public EncodedImage getPhoto() {
		return getImage(IMAGE_NAME);
	}

	private EncodedImage getImage(String imageName) {
		int imageHeight = Display.getHeight() - 60;

		return Utilities.getScaledImage(imageName, imageHeight);
	}

	private EncodedImage setImageFromCamera(String path) throws Exception {
		FileConnection fconn = (FileConnection) Connector
				.open("file://" + path);

		System.out.println("Setting photo at " + path);

		InputStream input = null;
		input = fconn.openInputStream();

		int fSz = (int) fconn.fileSize();
		byte[] data = new byte[fSz];

		input.read(data, 0, fSz);
		EncodedImage ei = EncodedImage.createEncodedImage(data, 0, data.length);

		return ei;
	}

	private class JournalListener implements FileSystemJournalListener {

		public void fileJournalChanged() {
			long nextUSN = FileSystemJournal.getNextUSN();
			String msg = null;

			for (long lookUSN = nextUSN - 1; lookUSN >= _lastUSN && msg == null; --lookUSN) {
				FileSystemJournalEntry entry = FileSystemJournal
						.getEntry(lookUSN);

				// We didn't find an entry.
				if (entry == null) {
					break;
				}

				try {
					Thread.sleep(500);
				} catch (Exception e) {
				}

				// Try to kill camera app here by injecting esc.
				EventInjector.KeyEvent inject = new EventInjector.KeyEvent(
						EventInjector.KeyEvent.KEY_DOWN, Characters.ESCAPE, 50);
				inject.post();
				inject.post();

				// Check if this entry was added or deleted.
				String path = entry.getPath();

				if (path != null) {
					switch (entry.getEvent()) {
					case FileSystemJournalEntry.FILE_ADDED:
						try {
							EncodedImage encodedImage = setImageFromCamera(path);

							if (listener != null) {
								listener.handlePhoto(encodedImage);
							}
						} catch (Exception e) {
							System.out
									.println("Error getting image from camera: "
											+ e);
						}
						break;
					}
				}

				_lastUSN = nextUSN;
			}
		}
	}
}
