package com.rapidftr.utilities;

import net.rim.device.api.util.StringUtilities;

public class Properties {

	private static final int DEFAULT_PORT = 80;
	private static final String DEFAULT_HOST = "dev.rapidftr.com";
	private static final int DEFAULT_HTTP_TIMEOUT = 10000;

	private static Properties instance;
	private String hostName = DEFAULT_HOST;
	private int httpRequestTimeout;
	private int port = DEFAULT_PORT;

	public static synchronized Properties getInstance() {
		if (instance == null) {
			instance = new Properties();
		}

		return instance;
	}

	private Properties() {
		httpRequestTimeout = DEFAULT_HTTP_TIMEOUT;
	}

}
