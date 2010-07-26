package com.rapidftr.datastore;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class FormStore {
	
	static final long KEY = 0x6699d842f70a9c2cL ; //com.rapidftr.datastore.FormStore
	
	
	public Hashtable getEnabledForms()
    {
    	PersistentObject persistentObject = PersistentStore.getPersistentObject(KEY);
		return (Hashtable) persistentObject.getContents();
    }
	
	public void synchronizeWithServer()
	{
		
	}
	
}
