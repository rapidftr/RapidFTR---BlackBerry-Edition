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

import java.io.IOException;

import javax.microedition.io.HttpConnection;

import com.sun.me.web.path.Result;

public class Response {

    Result result = null;
    int responseCode = HttpConnection.HTTP_NOT_FOUND;
    Exception ex = null;
    String contentType = null;
    String charset = null;
    Arg[] headers = null;

    public Response() {
    }

    public Response(Result result, int code) {
        this.result = result;
        this.responseCode = code;
    }

    public Result getResult() {
        return result;
    }

    public int getCode() {
        return responseCode;
    }

    public Arg[] getHeaders() {
        return headers;
    }

    public Exception getException() {
        return ex;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getCharset() {
        return charset;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public void setHeaders(Arg[] headers) {
        this.headers = headers;
    }

    public void setException(Exception exception) {
        this.ex = exception;
    }

    public String getErrorMessage() {
        final int responseCode = getResponseCode();
        if (responseCode == HttpConnection.HTTP_FORBIDDEN ||
                getResponseCode() == HttpConnection.HTTP_UNAUTHORIZED) {
            return "Authentication Failure";
        }
        final Exception exception = getException();
        if (exception != null && exception instanceof IOException) {
            return "Could not connect";
        }
        return "Error occurred";
    }
}
