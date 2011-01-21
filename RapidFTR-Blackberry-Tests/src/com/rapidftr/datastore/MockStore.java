package com.rapidftr.datastore;

import com.rapidftr.utilities.Store;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

import java.util.Vector;

public class MockStore implements Store {

	private Hashtable table = new Hashtable();
	
	public MockStore() {
	}

	public void clear() {

	}

	public String getString(String key) {
		return (String)table.get(key);
	}

	public String getString(String key, String defaultValue) {
		if(table.containsKey(key))
			return (String)table.get(key);
		
		return defaultValue;
	}

	public void remove(String key) {
		table.remove(key);

	}

	public void setString(String key, String value) {
		table.put(key, value);
	}

	public Vector<?> getVector(String key) {
		if(table.containsKey(key))
			return (Vector<?>)table.get(key);
		return new Vector();
	}
	

	public void setVector(String key, Vector value) {
		table.put(key, value);
	}

}
