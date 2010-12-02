package com.rapidftr.datastore;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.CodeSigningKey;
import net.rim.device.api.system.ControlledAccess;
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
		int moduleHandle = ApplicationDescriptor.currentApplicationDescriptor().getModuleHandle();
		CodeSigningKey codeSigningKey = CodeSigningKey.get(moduleHandle, "RFTR");
		persistentObject.setContents(new ControlledAccess(contents, codeSigningKey));
		persistentObject.commit();
	}
}
