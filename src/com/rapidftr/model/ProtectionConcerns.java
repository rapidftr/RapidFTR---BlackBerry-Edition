package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class ProtectionConcerns implements Persistable {
	public static final String[] NAMES = { "Unaccompanied", "Refugee",
			"Internally Displaced", "Trafficked Child", "Orphan",
			"In Interim Care", "Child-headed Household", "Sick/Injured",
			"Physical/Sexual Abuse", "Disability" };

	public static final int UNACCOMPANIED = 0;
	public static final int REFUGEE = 1;
	public static final int INTERNALLY_DISPL = 2;
	public static final int TRAFFICKED = 3;
	public static final int ORPHAN = 4;
	public static final int INTERIM_CARE = 5;
	public static final int CHILD_HEADED_HSE = 6;
	public static final int SICK_INJURED = 7;
	public static final int PHYS_SEXUAL_ABUSE = 8;
	public static final int DISABILITY = 9;
	
	private ProtectionConcern concerns[] = new ProtectionConcern[NAMES.length];

	public ProtectionConcerns() {
		for ( int i=0; i<concerns.length; i++ ) {
			concerns[i] = new ProtectionConcern();
			concerns[i].setName(NAMES[i]);
		}
	}
	
	public ProtectionConcern[] getConcerns() {
		return concerns;
	}

	public void setConcerns(ProtectionConcern[] concerns) {
		this.concerns = concerns;
	}

	public void setConcern(int index, boolean status) {
		concerns[index].status = status;
	}

	public class ProtectionConcern implements Persistable {
		private boolean status;
		private String name;

		public boolean isStatus() {
			return status;
		}

		public void setStatus(boolean status) {
			this.status = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
}
