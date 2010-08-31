package com.rapidftr.model;

public class SearchChildFilter {
	private String name;
	private String id;
	
	public SearchChildFilter(String Name, String id) {
		this.name=Name;
		this.id=id;
	}

	public SearchChildFilter() {
		name="";
		id="";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
