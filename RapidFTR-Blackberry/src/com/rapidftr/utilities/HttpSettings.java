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

	public void setHost(String host) {
			settings.setLastUsedLoginHost(host);
	}
	
}
