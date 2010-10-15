package com.rapidftr.net;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rapidftr.utilities.Settings;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import static org.mockito.Mockito.when;

public class HttpServiceTest {

	private HttpService httpService;
	@Mock
	private HttpServer httpServer;
	@Mock
	private RequestListener listener;
	private String url;
	@Mock
	private Object context;
	@Mock
	Settings settings;
	private Arg[] httpArgs;
	private Arg[] httpArgsWithAuthToken;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		httpService = new HttpService(httpServer, settings);
		url = "url";
		String token = "token";
		httpArgs = new Arg[0];
		httpArgsWithAuthToken = new Arg[] { new Arg(Arg.AUTHORIZATION,
				"RFTR_Token " + token) };
		when(settings.getAuthorizationToken()).thenReturn(token);
		when(httpServer.buildFullyQualifiedUrl(url)).thenReturn(url);
	}

	@Test
	public void shouldSendPostResquestToHttpServer() {
		Arg[] postParams = new Arg[0];
		Arg[] postArgs = new Arg[0];
		PostData postData = mock(PostData.class);
		httpService
				.post(url, postParams, postArgs, listener, postData, context);
		verify(httpServer).postToServer(url, postParams, httpArgsWithAuthToken,
				listener, postData, context);
	}

	@Test
	public void shouldSendGetRequestToHttpServer() {
		Arg[] inputParams = new Arg[0];
		httpService.get(url, inputParams, httpArgs, listener,null);
		verify(httpServer).getFromServer(url, inputParams,
				httpArgsWithAuthToken, listener,null);
	}

	@Test
	public void shouldCancelCurrentRequest() {
		httpService.cancelRequest();
		verify(httpServer).cancelRequest();
	}

}
