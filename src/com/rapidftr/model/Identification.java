package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class Identification implements Persistable {	
	public static final int SEP_1_2_WKS = 0;
	public static final int SEP_2_4_WKS = 1;
	public static final int SEP_1_6_MTHS = 2;
	public static final int SEP_6_MTHS_1_YR = 3;
	public static final int SEP_GT_1_YR = 4;
	
	private static final String separationDates[] = {
		"1 - 2 Weeks",
		"2 - 4 Weeks",
		"1 - 6 Months",
		"6 Months - 1 Year",
		"> 1 Year"
	};
	

	private String name;
	private boolean sex;
	private int age;
	private boolean isExactAge;
	private String origin;
	private String lastKnownLocation;
	private int dateOfSeparation;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isSex() {
		return sex;
	}
	
	public void setSex(boolean sex) {
		this.sex = sex;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public boolean isExactAge() {
		return isExactAge;
	}
	public void setExactAge(boolean isExactAge) {
		this.isExactAge = isExactAge;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getLastKnownLocation() {
		return lastKnownLocation;
	}
	public void setLastKnownLocation(String lastKnownLocation) {
		this.lastKnownLocation = lastKnownLocation;
	}
	public int getDateOfSeparation() {
		return dateOfSeparation;
	}
	public void setDateOfSeparation(int dateOfSeparation) {
		this.dateOfSeparation = dateOfSeparation;
	}
	
	public static String getFormattedSeparationDate(int dateOfSeparation) {
		return separationDates[dateOfSeparation];
	}
	
}
