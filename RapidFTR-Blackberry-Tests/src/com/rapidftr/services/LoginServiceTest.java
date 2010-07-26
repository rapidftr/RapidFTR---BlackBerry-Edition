package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Hashtable;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.controllers.LoginController;
import com.rapidftr.net.HttpService;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginServiceTest {

	private LoginService loginService;
	private LoginServiceListener listener;
	private HttpService httpService;
	private Object context;

	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		loginService = new LoginService(httpService);
		loginService.setListener(listener);
		listener = mock(LoginServiceListener.class);
		loginService.setListener(listener);
	}

	@Test
	public void shouldPostToServerWithProperUrlAndParams() {

		String userName = "xxxxx";
		String password = "yyyyy";
		loginService.login(userName, password);
		Arg[] postParams = new Arg[] { new Arg("user_name", userName),
				new Arg("password", password) };

		Hashtable context = new Hashtable();

		context.put(LoginController.USER_NAME, userName);

		final Arg acceptJson = new Arg("Accept", "application/json");
		final Arg[] httpParams = { acceptJson };

		verify(httpService).post("sessions", postParams, loginService, context);
	}

	@Test
	public void shouldSendAuthoriFailedMessageToLoginSerivceListenerOnFailure()
			throws Exception {

		Response failedLoginResponse = stubFailedLoginResponse();

		loginService.done(mock(Object.class), failedLoginResponse);

		verify(listener).onAuthenticationFailure();
	}

	@Test
	public void shouldSendAuthorizationTokenToLoginControllerOnSucees()
			throws Exception {

		String authorizationToken = "token";
		Response successfulLoginResponse = stubSuccessfulResponseWithToken(authorizationToken);

		context = mock(Object.class);
		loginService.done(context, successfulLoginResponse);

		verify(listener).onLoginSucees(context, authorizationToken);
	}

	@Test
	public void shouldSendConnectionProblemMessageToLoginControllerOnIoException()
			throws Exception {

		Response response = mock(Response.class);
		when(response.getException()).thenReturn(new IOException());

		loginService.done(context, response);

		verify(listener).onConnectionProblem();
	}

	@Test
	public void shouldCancelRequest() {
		loginService.cancelLogin();
		verify(httpService).cancelRequest();
	}

	private Response stubSuccessfulResponseWithToken(String authorizationToken)
			throws ResultException {
		Response response = mock(Response.class);
		String jsonLoggedInString = String
				.format(
						"{\"session\":{\"link\":{\"uri\":\"/sessions/4b655b458549a8940675304082179c76\",\"rel\":\"session\"},\"token\":\"%s\"}}",
						authorizationToken);
		when(response.getResult()).thenReturn(
				Result.fromContent(jsonLoggedInString, "application/json"));
		when(response.getCode()).thenReturn(201);
		return response;
	}

	private Response stubFailedLoginResponse() throws ResultException {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(406);
		return response;
	}

}
