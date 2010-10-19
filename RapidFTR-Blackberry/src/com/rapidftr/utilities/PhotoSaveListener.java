package com.rapidftr.utilities;

import net.rim.device.api.io.file.FileSystemJournal;
import net.rim.device.api.io.file.FileSystemJournalEntry;
import net.rim.device.api.io.file.FileSystemJournalListener;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.EventInjector.KeyEvent;

public abstract class PhotoSaveListener implements FileSystemJournalListener {
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

	protected abstract void photo(String path);
}
