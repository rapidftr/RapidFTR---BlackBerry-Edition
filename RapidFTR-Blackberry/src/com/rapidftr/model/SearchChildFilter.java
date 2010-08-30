package com.rapidftr.model;

public class SearchChildFilter {
	private String Name;
	private String id;
	
	public SearchChildFilter(String Name, String id) {
		this.Name=Name;
		this.id=id;
	}

	public SearchChildFilter() {
		Name="";
		id="";
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
