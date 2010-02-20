package com.rapidftr.utilities;


public class Properties {

	public static final int CONNECTION_WIFI = 0;
	public static final int CONNECTION_BIS = 1;
	public static final int CONNECTION_TCPIP = 2;
	
	private static final String DEFAULT_HOST = "madeleine";
	
	private static Properties instance;

	private String hostName;
	private boolean useCamera;
	private int connectionType;
	
	public static synchronized Properties getInstance() {
		if (instance == null) {
			instance = new Properties();
		}

		return instance;
	}

	private Properties() {
		useCamera = false;
	}
	
	public String getHostName() {
		return ((hostName == null) || (hostName.trim().length() == 0)) ? DEFAULT_HOST : hostName;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public boolean isUseCamera() {
		return useCamera;
	}

	public void setUseCamera(boolean useCamera) {
		this.useCamera = useCamera;
	}

	public int getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(int connectionType) {
		this.connectionType = connectionType;
	}
	
	
}
