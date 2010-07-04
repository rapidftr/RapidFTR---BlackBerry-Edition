package com.rapidftr.services;

import com.rapidftr.utilities.HttpServer;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginServiceImpl implements LoginService {
    private final HttpServer httpServer;

    public LoginServiceImpl(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public String login(String userName, String password) throws LoginFailedException {
        Arg[] postParams = new Arg[]{
                new Arg("user_name", userName),
                new Arg("password", password)};

        Response response = httpServer.postToServer(postParams);
        if (response.getCode() != 201) {
            throw new LoginFailedException();
        }
        return parseAuthorizationToken(response);
    }

    private String parseAuthorizationToken(Response response) {
        try {
            return response.getResult().getAsString("session.token");
        } catch (ResultException e) {
            throw new ServiceException("JSON returned from login service in unexpected format");
        }
    }


}
