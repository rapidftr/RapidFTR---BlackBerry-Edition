package com.rapidftr.utilities;

public class Properties {

	private static final int DEFAULT_PORT = 3000;
	private static final String DEFAULT_HOST = "dev.rapidftr.com";

	private static final int DEFAULT_HTTP_TIMEOUT = 1000000;

	private static Properties instance;
	private String hostName = null;
	private int httpRequestTimeout;

	public static synchronized Properties getInstance() {
		if (instance == null) {
			instance = new Properties();
		}

		return instance;
	}

	private Properties() {
		httpRequestTimeout = DEFAULT_HTTP_TIMEOUT;
	}

	public String getHostName() {
		return ((hostName == null) || (hostName.trim().length() == 0)) ? DEFAULT_HOST
				: hostName;
	}

	public int getPort() {
		return DEFAULT_PORT;
	}

	public int getHttpRequestTimeout() {
		return httpRequestTimeout;
	}

}
