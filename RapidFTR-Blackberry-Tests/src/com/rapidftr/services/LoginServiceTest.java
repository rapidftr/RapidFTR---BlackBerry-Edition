package com.rapidftr.services;

import com.rapidftr.controllers.LoginController;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.HttpService;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import static org.mockito.Mockito.*;

public class LoginServiceTest {

	private LoginService loginService;
	private HttpRequestHandler listener;
	private HttpService httpService;
	private Object context;
	private RequestCallBack requestCallback;

	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		loginService = new LoginService(httpService);
		requestCallback = mock(RequestCallBackImpl.class);
		listener = new HttpRequestHandler(requestCallback);
		loginService.setListener(listener);
		context = mock(Object.class);
	}

	@Test
	public void shouldPostToServerWithProperUrlAndParams() {

		String userName = "xxxxx";
		String password = "yyyyy";
		Arg[] postParams = new Arg[] { new Arg("user_name", userName),
				new Arg("password", password) };

		Hashtable context = new Hashtable();

		context.put(LoginController.USER_NAME, userName);

		final Arg acceptJson = HttpUtility.HEADER_ACCEPT_JSON;
		final Arg[] httpParams = { acceptJson };
		
		loginService.login(userName, password);


		verify(httpService).post("sessions" ,postParams,httpParams,listener, null, context);
	}

	@Test
	public void shouldSendAuthoriFailedMessageToLoginSerivceListenerOnFailure()
			throws Exception {

		Response failedLoginResponse = stubFailedLoginResponse();
        listener.setRequestInProgress();
		listener.done(new Object(), failedLoginResponse);
		verify(requestCallback).handleUnauthorized();
	}

	@Test
	public void shouldSendAuthorizationTokenToLoginControllerOnSucees()
			throws Exception {

		String authorizationToken = "token";
		Response successfulLoginResponse = stubSuccessfulResponseWithToken(authorizationToken);
        listener.setRequestInProgress();
		listener.done(context, successfulLoginResponse);
		verify(requestCallback).onSuccess(context, successfulLoginResponse);
	}

	@Test
	public void shouldSendConnectionProblemMessageToLoginControllerOnIoException()
			throws Exception {

		Response response = mock(Response.class);
		when(response.getException()).thenReturn(new IOException());
        listener.setRequestInProgress();
		listener.done(context, response);
		verify(requestCallback).handleException(response.getException());
	}

	@Test
	public void shouldCancelRequest() {
		loginService.cancelLogin();
		verify(httpService).cancelRequest();
	}

	private Response stubSuccessfulResponseWithToken(String authorizationToken)
			throws ResultException {
		Response response = mock(Response.class);
        String jsonLoggedInString = "{\"session\":{\"link\":{\"uri\":\"/sessions/4b655b458549a8940675304082179c76\",\"rel\":\"session\"},\"token\":\"" + authorizationToken + "\"}}";
		when(response.getResult()).thenReturn(
				Result.fromContent(jsonLoggedInString, "application/json"));
		when(response.getCode()).thenReturn(HttpConnection.HTTP_OK);
		return response;
	}

	private Response stubFailedLoginResponse() throws ResultException {
		Response response = mock(Response.class);
		when(response.getCode()).thenReturn(HttpConnection.HTTP_UNAUTHORIZED);
		return response;
	}

}
