package com.rapidftr.services;

public interface LoginService {
	boolean login(String hostName, String userName, String password) throws ServiceException;

	String getLoggedInUser();
	
	String getLoggedInFullName();
}
