package com.rapidftr.services;

public interface LoginService {
	String login(String hostName, String userName, String password) throws ServiceException;

	String getLoggedInUser();
	
	String getLoggedInFullName();

    String login(String userName, String password) throws ServiceException;
}
