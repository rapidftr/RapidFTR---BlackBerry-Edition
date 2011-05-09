package com.rapidftr.net;

import static org.mockito.Mockito.mock;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.sun.me.web.request.Request;

public class RequestPoolTest {
	private RequestPool requestPool;
	private Request request;
    private HttpGateway httpGateway;

	@Before
	public void setUp() {

        httpGateway = mock(HttpGateway.class);
        requestPool = RequestPool.getInstance(httpGateway);
        requestPool.cancelAllRequests(); 
		request = Request.createPostRequest("", null, null, null, null, null);
	}

	@Test
	public void atAnyTimeActiveThreadsShouldNotBeMoreThenMaximumActiveThreads() {
		for (int i = 0; i < RequestPool.MAXIMUM_ACTIVE_THREADS * 10; i++) {
			requestPool.execute(request);
			Assert
					.assertTrue(requestPool.activeThreads <= RequestPool.MAXIMUM_ACTIVE_THREADS);
		}
	}

}
