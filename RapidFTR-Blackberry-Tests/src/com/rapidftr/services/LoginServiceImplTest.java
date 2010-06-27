package com.rapidftr.services;

import com.rapidftr.utilities.HttpServer;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginServiceImplTest {

    @Test
    public void should_post_to_server_and_extract_auth_token_from_response() throws Exception {

        HttpServer httpServer = mock(HttpServer.class);
        Response successfulResponse = stubSuccessfulResponseWithToken("authorization_token");
        when(httpServer.postToServer(new Arg[]{new Arg("user_name", "rapidftr"), new Arg("password", "rapidftr")}))
                .thenReturn(successfulResponse);
        LoginService loginService = new LoginServiceImpl(httpServer);

        String token = loginService.login("rapidftr", "rapidftr");

        assertThat(token, is("authorization_token"));
    }

    @Test(expected = LoginFailedException.class)
    public void should_throw_login_failed_exception_if_login_fails() throws Exception {
        HttpServer httpServer = mock(HttpServer.class);
        Response failedLoginResponse = stubFailedLoginResponse();
        when(httpServer.postToServer(new Arg[]{new Arg("user_name", "rapidftr"), new Arg("password", "invalid_password")}))
                .thenReturn(failedLoginResponse);
        LoginService loginService = new LoginServiceImpl(httpServer);

        loginService.login("rapidftr", "invalid_password");
    }

    private Response stubSuccessfulResponseWithToken(String authorizationToken) throws ResultException {
        Response response = mock(Response.class);
        String jsonLoggedInString = String.format("{\"session\":{\"link\":{\"uri\":\"/sessions/4b655b458549a8940675304082179c76\",\"rel\":\"session\"},\"token\":\"%s\"}}", authorizationToken);
        when(response.getResult()).thenReturn(Result.fromContent(jsonLoggedInString, "application/json"));
        when(response.getCode()).thenReturn(201);
        return response;
    }

    private Response stubFailedLoginResponse() throws ResultException {
        Response response = mock(Response.class);
        when(response.getCode()).thenReturn(406);
        return response;
    }
}
