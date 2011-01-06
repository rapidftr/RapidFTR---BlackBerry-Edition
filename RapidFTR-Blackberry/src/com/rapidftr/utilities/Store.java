package com.rapidftr.utilities;

import java.util.Vector;

public interface Store {

	public abstract void setString(String key, String value);

	public abstract String getString(String key);

	public abstract String getString(String key, String defaultValue);

	public abstract void clear();

	public abstract void remove(String key);

	public abstract Vector getVector(String key);

	public abstract void setVector(String key, Vector value);


}