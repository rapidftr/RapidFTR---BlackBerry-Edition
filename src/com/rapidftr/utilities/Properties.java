package com.rapidftr.utilities;


public class Properties {

	public static final int CONNECTION_WIFI = 0;
	public static final int CONNECTION_BIS = 1;
	public static final int CONNECTION_TCPIP = 2;
	
	//private static final String DEFAULT_HOST = "chidmzhcf01.thoughtworks.com";
	
	private static final String DEFAULT_HOST = "97.107.135.7"; 
	
	private static final int DEFAULT_HTTP_TIMEOUT = 20000;
	
	private static Properties instance;

	private String hostName;
	private int port;
	private String authenticityToken;
	private String sessionCookie;
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
		return ((hostName == null) || (hostName.trim().length() == 0)) ? DEFAULT_HOST : hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	
	public int getPort() {
		return 3000;
		//return ((hostName.length() == 0) || (hostName.equals(DEFAULT_HOST))) ? 80 : 3000;
	}

	public String getAuthenticityToken() {
		return authenticityToken;
	}

	public void setAuthenticityToken(String authenticityToken) {
		this.authenticityToken = authenticityToken;
	}

	public String getSessionCookie() {
		return sessionCookie;
	}

	public void setSessionCookie(String sessionCookie) {
		this.sessionCookie = sessionCookie;
	}

	public int getHttpRequestTimeout() {
		return httpRequestTimeout;
	}

	public void setHttpRequestTimeout(int httpRequestTimeout) {
		this.httpRequestTimeout = httpRequestTimeout;
	}
	
	
}
