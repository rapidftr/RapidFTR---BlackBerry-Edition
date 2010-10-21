package com.rapidftr;

public class Key {

	
	private final String key;

	public Key(String key) {
		this.key = key;
	}

	public int getValue() {
		return key.hashCode();
	}

}
