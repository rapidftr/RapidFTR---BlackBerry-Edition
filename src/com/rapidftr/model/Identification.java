package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class Identification implements Persistable {
	public static final int SEP_1_2_WKS = 0;
	public static final int SEP_2_4_WKS = 1;
	public static final int SEP_1_6_MTHS = 2;
	public static final int SEP_6_MTHS_1_YR = 3;
	public static final int SEP_GT_1_YR = 4;

	public static final String[] separationDates = { "1-2 weeks ago",
			"2-4 weeks ago", "1-6 months ago", "6 months to 1 year ago",
			"More than 1 year ago" };

	private String name;
	private boolean male;
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

	public boolean isMale() {
		return male;
	}

	public void setMale(boolean male) {
		this.male = male;
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

	public void setDateOfSeparation(String dateOfSeparation) {
		for ( int i=0; i<separationDates.length; i++ ) {
			if ( dateOfSeparation.equals(separationDates[i])) {
				this.dateOfSeparation = i;
				break;
			}
		}
	}
	
	public void setDateOfSeparation(int dateOfSeparation) {
		this.dateOfSeparation = dateOfSeparation;
	}

	public String getFormattedSeparationDate() {
		return (dateOfSeparation == -1) ? ""
				: separationDates[dateOfSeparation];
	}

}
