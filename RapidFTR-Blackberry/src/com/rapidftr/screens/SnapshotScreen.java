package com.rapidftr.screens;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.device.api.system.EncodedImage;
import net.rim.device.api.ui.UiApplication;
import com.rapidftr.controllers.SnapshotController;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.utilities.ImageEncoder;
import com.rapidftr.utilities.PhotoSaveListener;

public class SnapshotScreen extends CustomScreen {

	private PhotoSaveListener listener;

	public SnapshotScreen() {
		super();
		listener = new PhotoSaveListener() {

			protected void photo(String path) {
				try {
					((SnapshotController) controller).capturedImage(path,
							new ImageEncoder().getEncodedImage(path));
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

	
}


