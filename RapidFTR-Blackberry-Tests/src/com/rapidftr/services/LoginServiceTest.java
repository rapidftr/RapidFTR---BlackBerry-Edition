package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.HttpService;
import com.rapidftr.net.RequestCallBack;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class LoginServiceTest {

	private LoginService loginService;
	private HttpService httpService;
	private SettingsStore settingsStore;
	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		settingsStore = mock(SettingsStore.class);
		loginService = new LoginService(httpService,settingsStore);
	}


	@Test
	public void shouldSaveTheLastEnteredUsernameAndAuthorizationTokenAndPopOutLoginScreenInorderToGetBackToHomeScreen() throws Exception {

		loginService.login("abcd", "abcd"); // Just to make the
												// isRequestInProgress flag to
												// set
		Hashtable context = new Hashtable();
		String userName = "name";
		context.put(LoginService.USER_NAME, userName);

		String authorisationToken = "token";
		//loginController.on(context, authorisationToken);
		Response response = mock(Response.class);
		Result result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.getAsString("session.token")).thenReturn("token");
		loginService.onRequestSuccess(context, response);

		verify(settingsStore).setLastUsedUsername(userName);
		verify(settingsStore).setAuthorisationToken(authorisationToken);
	}
	@Test
	public void shouldPostToServerWithProperUrlAndParams() {

		String userName = "xxxxx";
		String password = "yyyyy";
		Arg[] postParams = new Arg[] { new Arg("user_name", userName),
				new Arg("password", password) };

		Hashtable context = new Hashtable();

		context.put(LoginService.USER_NAME, userName);

		final Arg acceptJson = HttpUtility.HEADER_ACCEPT_JSON;
		final Arg[] httpParams = { acceptJson };
		
		loginService.login(userName, password);


		verify(httpService).post("sessions" ,postParams,httpParams,loginService.requestHandler, null, context);
	}

//	@Test
//	public void shouldSendAuthoriFailedMessageToLoginSerivceListenerOnFailure()
//			throws Exception {
//
//		Response failedLoginResponse = stubFailedLoginResponse();
//        listener.setRequestInProgress();
//		listener.done(new Object(), failedLoginResponse);
//		verify(requestCallback).handleUnauthorized();
//	}

//	@Test
//	public void shouldSendAuthorizationTokenToLoginControllerOnSucees()
//			throws Exception {
//
//		String authorizationToken = "token";
//		Response successfulLoginResponse = stubSuccessfulResponseWithToken(authorizationToken);
//        listener.setRequestInProgress();
//		listener.done(context, successfulLoginResponse);
//		verify(requestCallback).onSuccess(context, successfulLoginResponse);
//	}

	
	@Test
	public void shouldCancelRequest() {
		loginService.cancelRequest();
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
