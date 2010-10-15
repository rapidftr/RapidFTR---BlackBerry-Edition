package com.rapidftr.utilities;

public class HttpSettings {

	private final Settings settings;

	public HttpSettings(Settings settings) {
		this.settings = settings;
	}

	public int getTimeOut() {
		return settings.getTimeOut();
	}

	public String getHost() {
		return settings.getLastUsedLoginHost();
	}

	public int getPort() {
		return Integer.parseInt(settings.getLastUsedLoginPort());
	}

	public void setHost(String host) {
			settings.setLastUsedLoginHost(host);
	}
	
	public void setPort(String port) {
			settings.setLastUsedLoginPort(port);
	}
	
}
