package com.rapidftr.utilities;

import com.rapidftr.services.RecordService;
import com.rapidftr.services.impl.RecordServiceImpl;
import com.rapidftr.utilities.impl.LocalStoreImpl;

public class Properties {

	private static final String DEFAULT_HOST = "madeleine";
	
	private static Properties instance;

	private String hostName;
	
	public static synchronized Properties getInstance() {
		if (instance == null) {
			instance = new Properties();
		}

		return instance;
	}

	private Properties() {
	}
	
	public String getHostName() {
		return ((hostName == null) || (hostName.trim().length() == 0)) ? DEFAULT_HOST : hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
}
