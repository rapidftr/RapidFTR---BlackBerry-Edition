package com.rapidftr.net;

import com.sun.me.web.request.PostData;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.sun.me.web.request.Request;

public class RequestPoolTest {
	private RequestPool requestPool;
	Request request;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		requestPool = RequestPool.getInstance();
		request = Request.post("", null, null, null, null, (PostData) null, null);
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
