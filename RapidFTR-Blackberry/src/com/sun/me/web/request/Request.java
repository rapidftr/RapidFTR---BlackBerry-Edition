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

import com.rapidftr.utilities.Arrays;

public final class Request {

	private Object context = null;
	private String url = null;
	private String method = null;
	private Arg[] httpArgs = null;
	private Arg[] inputArgs = null;
	private PostData multiPart = null;
	private RequestListener listener = null;

	public static Request createGetRequest(final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final Object context) {

		return createRequest(HttpConnection.GET, url, inputArgs, httpArgs, listener, null, context);
	}

	public static Request createPostRequest(final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final PostData multiPart, final Object context) {

		return createRequest(HttpConnection.POST, url, inputArgs, httpArgs, listener, multiPart, context);
	}

    public static Request createPostRequest(String url, RequestListener listener, PostData postData) {
        return createRequest(HttpConnection.POST, url, null, null, listener, postData, null);
    }

    public static Request createGetRequest(String url, Arg[] inputParams, Arg[] httpArgs) {
        return createRequest(HttpConnection.GET, url, inputParams, httpArgs, null, null, null);
    }

	public static Request createGetRequest(String url) {
        return createRequest(HttpConnection.GET, url, null, null, null, null, null);
	}

	private static Request createRequest(final String method, final String url, final Arg[] inputArgs, final Arg[] httpArgs, final RequestListener listener, final PostData multiPart, final Object context) {

		final Request request = new Request();
		request.method = method;
		request.context = context;
		request.listener = listener;
		request.url = url;
		request.httpArgs = httpArgs;
		request.inputArgs = inputArgs;
		request.multiPart = multiPart;

		return request;
	}

    private Request() {

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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        if (context != null ? !context.equals(request.context) : request.context != null) return false;
        if (!Arrays.equals(httpArgs, request.httpArgs)) return false;
        if (!Arrays.equals(inputArgs, request.inputArgs)) return false;
        if (listener != null ? !listener.equals(request.listener) : request.listener != null) return false;
        if (method != null ? !method.equals(request.method) : request.method != null) return false;
        if (multiPart != null ? !multiPart.equals(request.multiPart) : request.multiPart != null) return false;
        if (url != null ? !url.equals(request.url) : request.url != null) return false;

        return true;
    }

    public int hashCode() {
        int result = context != null ? context.hashCode() : 0;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (method != null ? method.hashCode() : 0);
        result = 31 * result + (httpArgs != null ? Arrays.hashCode(httpArgs) : 0);
        result = 31 * result + (inputArgs != null ? Arrays.hashCode(inputArgs) : 0);
        result = 31 * result + (multiPart != null ? multiPart.hashCode() : 0);
        result = 31 * result + (listener != null ? listener.hashCode() : 0);
        return result;
    }

}
