package com.rapidftr.datastore;

import java.util.Vector;

import com.rapidftr.utilities.Store;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class MockStore implements Store {

	private Hashtable table = new Hashtable();
	
	public MockStore(String key) {
	}

	@Override
	public void clear() {

	}

	@Override
	public String getString(String key) {
		return (String)table.get(key);
	}

	@Override
	public String getString(String key, String defaultValue) {
		if(table.contains(key))
			return (String)table.get(key);
		
		return defaultValue;
	}

	@Override
	public void remove(String key) {
		table.remove(key);

	}

	@Override
	public void setString(String key, String value) {
		table.put(key, value);
	}

	@Override
	public Vector getVector(String key) {
		if(table.containsKey(key))
			return (Vector)table.get(key);
		return new Vector();
	}
	

	@Override
	public void setVector(String key, Vector value) {
		table.put(key, value);
	}

}
