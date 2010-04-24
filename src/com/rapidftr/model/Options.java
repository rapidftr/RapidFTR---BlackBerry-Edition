package com.rapidftr.model;

import net.rim.device.api.util.Persistable;

public class Options implements Persistable {
	public static final String[] NAMES = { "Reunification", "Follow-Up",
			"Outcome", "Displacement", "Health" };

	public static final int REUNIFICATION = 0;
	public static final int FOLLOW_UP = 1;
	public static final int OUTCOME = 2;
	public static final int DISPLACEMENT = 3;
	public static final int HEALTH = 4;

	private Option options[] = new Option[NAMES.length];

	public Options() {
		for (int i = 0; i < options.length; i++) {
			options[i] = new Option();
			options[i].setName(NAMES[i]);
		}
	}

	public Option[] getOptions() {
		return options;
	}

	public void setOptions(Option[] options) {
		this.options = options;
	}

	public void setOption(int index, boolean status) {
		options[index].status = status;
	}

	public class Option implements Persistable {
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
