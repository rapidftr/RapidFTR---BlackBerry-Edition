package com.sun.me.web.request;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


public class RequestPoolTest {
	private RequestPool requestPool;
	@Mock
	private WorkQueue requestQueue;
	Request request;
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		requestPool = RequestPool.getInstance();
		requestPool.requestQueue = requestQueue;
		request = mock(Request.class);
	}
	
	@Test
	public void onExecuteRequestShouldSubmittedToWorkQueue(){
		requestPool.execute(request);
		verify(requestQueue).addRequest(request);
		
	}

}
