package com.rapidftr.utilities;

import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class DefaultStore implements Store {
	private static final long PERSISTENT_STORE_KEY = "com.rapidftr.utilities.ftrstore"
			.hashCode();
	PersistentObject persistentObject = PersistentStore
			.getPersistentObject(PERSISTENT_STORE_KEY);

	protected void setContents(Hashtable hashtable) {
		persistentObject.setContents(hashtable);
	}

	protected Hashtable getContents() {
		Object contentsObject = (Hashtable) persistentObject.getContents();
		if (!(contentsObject instanceof Hashtable)) {
			setContents(new Hashtable());
		}

		return (Hashtable) persistentObject.getContents();
	}

	/* (non-Javadoc)
	 * @see com.rapidftr.utilities.Store#setString(java.lang.String, java.lang.String)
	 */
	public void setString(String key, String value) {
		if (value == null) {
			value = "";
		}
		getContents().put(key, value);
		commit();
	}

	/* (non-Javadoc)
	 * @see com.rapidftr.utilities.Store#getString(java.lang.String)
	 */
	public String getString(String key) {
		return getString(key, "");
	}

	/* (non-Javadoc)
	 * @see com.rapidftr.utilities.Store#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String defaultValue) {
		if (getContents().containsKey(key)) {
			return "" + getContents().get(key);
		} else {
			return defaultValue;
		}
	}

	/* (non-Javadoc)
	 * @see com.rapidftr.utilities.Store#clear()
	 */
	public void clear() {
		setContents(new Hashtable());
		commit();
	}

	private void commit() {
		persistentObject.commit();
	}

	/* (non-Javadoc)
	 * @see com.rapidftr.utilities.Store#remove(java.lang.String)
	 */
	public void remove(String key) {
		getContents().remove(key);
		commit();
	}

}
