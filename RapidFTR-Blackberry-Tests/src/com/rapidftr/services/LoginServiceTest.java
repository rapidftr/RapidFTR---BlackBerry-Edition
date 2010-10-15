package com.rapidftr.services;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import java.util.Hashtable;
import javax.microedition.io.HttpConnection;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.rapidftr.utilities.Settings;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

@Ignore("These tests fail as the store tries to get the persistent object from the persistent store which gives java.lang.unsatisfiedlink error")
public class LoginServiceTest {

	private LoginService loginService;
	private HttpService httpService;
	private Settings settings;

	@Before
	public void setUp() {
		httpService = mock(HttpService.class);
		settings = mock(Settings.class);
		loginService = new LoginService(httpService, settings);
	}

	@Test
	public void shouldSaveTheLastEnteredUsernameAndAuthorizationTokenAndPopOutLoginScreenInorderToGetBackToHomeScreen()
			throws Exception {

		loginService.login("abcd", "abcd"); // Just to
		// make
		// the
		// isRequestInProgress flag to
		// set
		Hashtable context = new Hashtable();
		String userName = "name";
		context.put(LoginService.USER_NAME, userName);

		String authorisationToken = "token";
		// loginController.on(context, authorisationToken);
		Response response = mock(Response.class);
		Result result = mock(Result.class);
		when(response.getResult()).thenReturn(result);
		when(result.getAsString("session.token")).thenReturn("token");
		loginService.onRequestSuccess(context, response);

		verify(settings).setLastUsedUsername(userName);
		verify(settings).setAuthorisationToken(authorisationToken);
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

		verify(httpService).post("sessions", postParams, httpParams,
				loginService.requestHandler, null, context);
	}

	@Test
	public void shouldCancelRequest() {
		loginService.cancelRequest();
		verify(httpService).cancelRequest();
	}

	@Test
	public void shouldCheckSettingStoreForAuthentication() throws Exception {
		Credential credential = new Credential("user", "pass", true, settings);
		loginService.login(credential);
		verify(settings).isVerifiedUser();
	}

	@Test
	public void shouldNotCallIsVerifiedUserIfNotInOfflineMode()
			throws Exception {
		Credential credential = new Credential("user", "pass", false, settings);
		loginService.login(credential);
		verify(settings, times(0)).isVerifiedUser();
	}

	@Test
	public void shouldPostDataToServerIfNotOffline() throws Exception {
		Credential credential = new Credential("user", "pass", false, settings);
		loginService.login(credential);

	}

	private Response stubSuccessfulResponseWithToken(String authorizationToken)
			throws ResultException {
		Response response = mock(Response.class);
		String jsonLoggedInString = "{\"session\":{\"link\":{\"uri\":\"/sessions/4b655b458549a8940675304082179c76\",\"rel\":\"session\"},\"token\":\""
				+ authorizationToken + "\"}}";
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
