/*
Copyright (c) 2007, Sun Microsystems, Inc.
 
All rights reserved.
 
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:
 
 * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in
      the documentation and/or other materials provided with the
      distribution.
 * Neither the name of Sun Microsystems, Inc. nor the names of its
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.sun.me.web.request;

import com.rapidftr.net.HttpGateway;

import javax.microedition.io.HttpConnection;
import java.io.*;

public final class Request implements Runnable {

    private static final boolean DEBUG = true;
    
	private Object context = null;
	private String url = null;
	private String method = null;
	private Arg[] httpArgs = null;
	private Arg[] inputArgs = null;
	private PostData multiPart = null;
	private RequestListener listener = null;
    private HttpGateway httpGateway;

	public static Response get(final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, HttpGateway httpGateway) throws IOException {

		return sync(HttpConnection.GET, url, inputArgs, httpArgs, listener, null, httpGateway);
	}

	private static Response sync(final String method, final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final PostData multiPart, HttpGateway httpGateway) throws IOException {

		final Request request = new Request();
		request.method = method;
		request.url = url;
		request.httpArgs = httpArgs;
		request.inputArgs = inputArgs;
		request.multiPart = multiPart;
		request.listener = listener;
        request.httpGateway = httpGateway;

		return httpGateway.perform(request);
	}

	public static Request get(final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final Object context, HttpGateway httpGateway) {

		return async(HttpConnection.GET, url, inputArgs, httpArgs, listener, null, context, httpGateway);
	}

	public static Request post(final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final PostData multiPart, final Object context, HttpGateway httpGateway) {

		return async(HttpConnection.POST, url, inputArgs, httpArgs, listener, multiPart, context, httpGateway);
	}

	private static Request async(final String method, final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final PostData multiPart, final Object context, HttpGateway httpGateway) {

		final Request request = new Request();
		request.method = method;
		request.context = context;
		request.listener = listener;
		request.url = url;
		request.httpArgs = httpArgs;
		request.inputArgs = inputArgs;
		request.multiPart = multiPart;
        request.httpGateway = httpGateway;

		// TODO: implement more sophisticated pooling, queuing and scheduling strategies
		// request.thread = new Thread(request);
		// request.thread.start();
		//request.pool.execute(request);
		return request;
	}

	private Request() {
	}

	public void run() {
        Response response = new Response();
		try {
		    response = httpGateway.perform(this);
		} catch (Exception ex) {
			response.ex = ex;
		} finally {
			if (listener != null) {
				try {
					listener.done(context, response);
				} catch (Throwable th) {
					if (DEBUG) {
						System.err.println("Uncaught throwable in listener: ");
						th.printStackTrace();
					}
				}
			}
		}
	}

    public Arg[] getInputArgs() {
        return inputArgs;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Arg[] getHttpArgs() {
        return httpArgs;
    }

    public PostData getPostData() {
        return multiPart;
    }

    public RequestListener getListener() {
        return listener;
    }

    public Object getContext() {
        return context;
    }
}
