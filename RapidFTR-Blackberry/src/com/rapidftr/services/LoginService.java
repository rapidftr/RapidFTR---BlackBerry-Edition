package com.rapidftr.services;

public interface LoginService {

    String login(String userName, String password) throws LoginFailedException;
}
