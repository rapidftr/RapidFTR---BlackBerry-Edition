package com.rapidftr.utilities;

import java.util.Hashtable;
import java.util.Vector;

import com.rapidftr.Key;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class DefaultStore implements Store {
	private PersistentObject persistentObject;

	public DefaultStore(Key key) {
		persistentObject = PersistentStore.getPersistentObject(key.getValue());
	}

	private void setContents(Hashtable hashtable) {
		persistentObject.setContents(hashtable);
	}

	private Hashtable getContents() {
		if (persistentObject.getContents() == null) {
			setContents(new Hashtable());
		}

		return (Hashtable) persistentObject.getContents();
	}


	public void setString(String key, String value) {
		if (value == null) {
			value = "";
		}
		put(key, value);
	}

	private void put(String key, Object value) {
		getContents().put(key, value);
		commit();
	}

	public String getString(String key) {
		return "" + get(key, "");
	}

	
	public String getString(String key, String defaultValue) {
		return "" + get(key, defaultValue);
	}

	
	public void clear() {
		setContents(new Hashtable());
		commit();
	}

	private void commit() {
		persistentObject.commit();
	}

	public void remove(String key) {
		getContents().remove(key);
		commit();
	}

	public Object get(String key, Object defaultValue) {
		if (getContents().containsKey(key)) {
			return getContents().get(key);
		}
		return defaultValue;
	}

	public Vector getVector(String key) {
		return (Vector) get(key,new Vector());
	}

	public void setVector(String key, Vector value) {
		put(key,value);
		
	}

}
