package com.rapidftr.utilities;

import java.util.Hashtable;

public interface Store {

	public abstract void setString(String key, String value);

	public abstract String getString(String key);

	public abstract String getString(String key, String defaultValue);

	public abstract void clear();

	public abstract void remove(String key);

}