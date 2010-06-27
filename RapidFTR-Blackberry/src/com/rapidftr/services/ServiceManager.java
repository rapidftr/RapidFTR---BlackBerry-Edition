package com.rapidftr.services;

public class ServiceManager {
	
	public static PhotoService getPhotoService() {
		return PhotoServiceImpl.getInstance();
	}
}
