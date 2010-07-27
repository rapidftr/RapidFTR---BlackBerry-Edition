package com.rapidftr.datastore;

import net.rim.device.api.system.PersistentObject;

public class PersistentStore {

	private PersistentObject persistentObject;

	public PersistentStore(long key) {
		persistentObject = net.rim.device.api.system.PersistentStore
				.getPersistentObject(key);
	}

	public Object getContents() {
		return persistentObject.getContents();
	}

	public void setContents(Object contents) {
		persistentObject.setContents(contents);
		persistentObject.commit();
	}

}
