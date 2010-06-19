package com.rapidftr.services;

import com.rapidftr.services.impl.LoginServiceImpl;
import com.rapidftr.services.impl.PhotoServiceImpl;

public class ServiceManager {
	
	public static PhotoService getPhotoService() {
		return PhotoServiceImpl.getInstance();
	}
}
