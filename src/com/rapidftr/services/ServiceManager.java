package com.rapidftr.services;

import com.rapidftr.services.impl.LoginServiceImpl;
import com.rapidftr.services.impl.RecordServiceImpl;

public class ServiceManager {
	public static RecordService getRecordService() {
		return RecordServiceImpl.getInstance();
	}
	
	public static LoginService getLoginService() {
		return LoginServiceImpl.getInstance();
	}
}
