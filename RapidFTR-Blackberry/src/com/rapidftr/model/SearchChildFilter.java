package com.rapidftr.model;

public class SearchChildFilter {
	private String name;
	
	public SearchChildFilter(String Name) {
		this.name=Name;
	}

	public SearchChildFilter() {
		name="";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
