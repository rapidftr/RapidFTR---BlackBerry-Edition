package com.rapidftr.net;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.RequestListener;
import static org.mockito.Mockito.when;

public class HttpServiceTest {

	private HttpService httpService;
	private HttpServer httpServer;
	private RequestListener listener;
	private String url;
	private Object context;

	@Before
	public void setUp() {
		httpServer = mock(HttpServer.class);
		
		httpService = new HttpService(httpServer);
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
				postParams, listener, context);
	}

	@Test
	public void shouldSendGetRequestToHttpServer() {
		Arg[] inputParams = new Arg[0];
		httpService.get(url, inputParams, listener);
		
		
		verify(httpServer).getFromServer(url,
				inputParams,  listener);
	}

	@Test
	public void shouldCancelCurrentRequest() {
		httpService.cancelRequest();
		verify(httpServer).cancelRequest();

	}

}
