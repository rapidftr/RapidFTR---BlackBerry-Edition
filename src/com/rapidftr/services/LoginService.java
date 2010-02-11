package com.rapidftr.services;

public interface LoginService {
	boolean login(String userName, String password) throws ServiceException;

	String getLoggedInUser();
}
