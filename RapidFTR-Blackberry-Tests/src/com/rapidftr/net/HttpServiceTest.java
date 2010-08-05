package com.rapidftr.net;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.rapidftr.utilities.SettingsStore;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import static org.mockito.Mockito.when;

public class HttpServiceTest {

	private HttpService httpService;
	private HttpServer httpServer;
	private RequestListener listener;
	private String url;
	private Object context;
	SettingsStore settingsStore;
	private Arg[] httpArgs;

	@Before
	public void setUp() {
		httpServer = mock(HttpServer.class);
		settingsStore = mock(SettingsStore.class);
		httpService = new HttpService(httpServer, settingsStore);
		listener = mock(RequestListener.class);
		url = "url";
		context = mock(Object.class);
		when(httpServer.buildFullyQualifiedUrl(url)).thenReturn(url);
	}

	@Test
	public void shouldSendPostResquestToHttpServer() {
	

		Arg[] postParams = new Arg[0];
		httpService.post(url, postParams , listener, context);
		
		verify(httpServer).postToServer(url,
				postParams, httpArgs, listener, context, null);
	}

	@Test
	public void shouldSendGetRequestToHttpServer() {
		Arg[] inputParams = new Arg[0];
		httpService.get(url, inputParams, listener);
		
		
		verify(httpServer).getFromServer(url,
				inputParams,httpArgs, listener);
	}

	@Test
	public void shouldCancelCurrentRequest() {
		httpService.cancelRequest();
		verify(httpServer).cancelRequest();

	}

}
