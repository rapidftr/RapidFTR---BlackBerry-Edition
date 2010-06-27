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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import com.sun.me.web.path.Result;

public final class Request implements Runnable {

    // Special URL for demo purposes
    public static final String DEMO_URL = "demo://";
    
    public static final String UTF8_CHARSET = "utf-8";
    
    private static final boolean DEBUG = true;
    
    private static final int BUFFER_SIZE = 512;
    
    private Object context = null;
    private String url = null;
    private String method = null;
    private Arg[] httpArgs = null;
    private Arg[] inputArgs = null;
    private PostData multiPart = null;
    private RequestListener listener = null;
    
    private Thread thread = null;
    private boolean interrupted = false;
    
    private int totalToSend = 0;
    private int totalToReceive = 0;
    private int sent = 0;
    private int received = 0;
    
    public static Response get(final String url,
        final Arg[] inputArgs,
        final Arg[] httpArgs,
        final RequestListener listener)
        throws IOException {
        
        return sync(HttpConnection.GET, url, inputArgs, httpArgs, listener, null);
    }
    
    public static Response post(final String url,
        final Arg[] inputArgs,
        final Arg[] httpArgs,
        final RequestListener listener,
        final PostData multiPart)
        throws IOException {
        
        return sync(HttpConnection.POST, url, inputArgs, httpArgs, listener, multiPart);
    }
    
    private static Response sync(
        final String method,
        final String url,
        final Arg[] inputArgs,
        final Arg[] httpArgs,
        final RequestListener listener,
        final PostData multiPart)
        throws IOException {
        
        final Request request = new Request();
        request.method = method;
        request.url = url;
        request.httpArgs = httpArgs;
        request.inputArgs = inputArgs;
        request.multiPart = multiPart;
        request.listener = listener;
        
        final Response response = new Response();
        request.doHTTP(response);
        return response;
    }
    
    public static void get(
        final String url,
        final Arg[] inputArgs,
        final Arg[] httpArgs,
        final RequestListener listener,
        final Object context) {
        
        async(HttpConnection.GET, url, inputArgs, httpArgs, listener, null, context);
    }
    
    public static void post(
        final String url,
        final Arg[] inputArgs,
        final Arg[] httpArgs,
        final RequestListener listener,
        final PostData multiPart,
        final Object context) {
        
        async(HttpConnection.POST, url, inputArgs, httpArgs, listener, multiPart, context);
    }
    
    private static void async(
        final String method,
        final String url,
        final Arg[] inputArgs,
        final Arg[] httpArgs,
        final RequestListener listener,
        final PostData multiPart,
        final Object context) {
        
        final Request request = new Request();
        request.method = method;
        request.context = context;
        request.listener = listener;
        request.url = url;
        request.httpArgs = httpArgs;
        request.inputArgs = inputArgs;
        request.multiPart = multiPart;
        
        // TODO: implement more sophisticated pooling, queuing and scheduling strategies
        request.thread = new Thread(request);
        request.thread.start();
    }
    
    private Request() {}
    
    public void cancel() {
        interrupted = true;
        // TODO: maybe wait a little to give the thread an opportunity to return cleanly
//#if CLDC!="1.0"
//#         if (thread != null) {
//#             thread.interrupt();
//#         }
//#endif
    }
    
    public void run() {
        final Response response = new Response();
        
        try {
            if (url.equals(DEMO_URL)) {
                doDemo(response);
            }
            else {
                doHTTP(response);
            }
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
    
    // data may be large, send in chunks while reporting progress and checking for interruption
    private void write(final OutputStream os, final byte[] data) throws IOException {
        
        if (interrupted) {
            return;
        }
        
        // optimization if a small amount of data is being sent
        if (data.length <= BUFFER_SIZE) {
            os.write(data);
            sent += data.length;
            if (listener != null) {
                try {
                    listener.writeProgress(context, sent, totalToSend);
                } catch (Throwable th) {
                    if (DEBUG) {
                        System.err.println("Uncaught throwable in listener: ");
                        th.printStackTrace();
                    }
                }
            }
        } else {
            int offset = 0;
            int length = 0;
            do {
                length = Math.min(BUFFER_SIZE, data.length - offset);
                if (length > 0) {
                    os.write(data, offset, length);
                    offset += length;
                    sent += length;
                    if (listener != null) {
                        try {
                            listener.writeProgress(context, sent, totalToSend);
                        } catch (Throwable th) {
                    if (DEBUG) {
                        System.err.println("Uncaught throwable in listener: ");
                        th.printStackTrace();
                    }
                        }
                    }
                }
            } while (!interrupted && length > 0);
        }
    }
    
    private void doHTTP(final Response response) throws IOException {
        
        final StringBuffer args = new StringBuffer();
        if (inputArgs != null) {
            if (inputArgs.length > 0) {
                for (int i=0; i < inputArgs.length; i++) {
                    if (inputArgs[i] != null) {
                        args.append(encode(inputArgs[i].getKey()));
                        args.append("=");
                        args.append(encode(inputArgs[i].getValue()));
                        if (i+1 < inputArgs.length && inputArgs[i+1] != null) {
                            args.append("&");
                        }
                    }
                }
            }
        }
        
        final StringBuffer location = new StringBuffer(url);
        if (HttpConnection.GET.equals(method) && args.length() > 0) {
            location.append("?");
            location.append(args.toString());
        }
        
        HttpConnection conn = null;
        try {
            conn = (HttpConnection) Connector.open(location.toString());
            conn.setRequestMethod(method);
            
            if (httpArgs != null) {
                for (int i=0; i < httpArgs.length; i++) {
                    if (httpArgs[i] != null) {
                        final String value = httpArgs[i].getValue();
                        if (value != null) {
                            conn.setRequestProperty(httpArgs[i].getKey(), value);
                        }
                    }
                }
            }
            
            if (interrupted) { return; }
            
            if (HttpConnection.POST.equals(method)) {
                OutputStream os = null;
                try {
                    os = conn.openOutputStream();
                    writePostData(args, os);
                } finally {
                    if (os != null) {
                        try { os.close(); } catch (IOException ignore) {}
                    }
                }
            }
            
            if (interrupted) { return; }
            
            copyResponseHeaders(conn, response);
            
            response.responseCode = conn.getResponseCode();
            if (response.responseCode != HttpConnection.HTTP_OK) {
            	// TODO: TomE: check for exceptional status codes
                // TODO: handle redirects
//                return;
            }
            
            if (interrupted) { return; }
            
            processContentType(conn, response);
            readResponse(conn, response);
        } finally {
            if (conn != null) { conn.close(); }
        }
        
    }
    
    /**
     * Return cached demo data (use instead of doHTTP())
     **/
    private void doDemo(final Response response) throws IOException {
        
        String content = null;
        if ((inputArgs != null) && (inputArgs.length > 0) && 
            (inputArgs[0] != null) && (inputArgs[0].getValue() != null)) {
            content = inputArgs[0].getValue();
        }
        else {
            throw new IOException("Invalid demo args");
        }
        
        response.charset = UTF8_CHARSET;
        response.contentType = "text/javascript";
        response.responseCode = HttpConnection.HTTP_OK;
        response.result = Result.fromContent(content, response.contentType);
    }
    
    private void writePostData(final StringBuffer args, final OutputStream os) throws IOException {
        if (multiPart != null) {
            final byte[] multipartBoundaryBits = multiPart.getBoundary().getBytes();
            final byte[] newline = "\r\n".getBytes();
            final byte[] dashdash = "--".getBytes();
            
            // estimate totalBytesToSend
            final Part[] parts = multiPart.getParts();
            for (int i=0; i < parts.length; i++) {
                final Arg[] headers = parts[i].getHeaders();
                for (int j=0; j < headers.length; j++) {
                    totalToSend += headers[j].getKey().getBytes().length;
                    totalToSend += headers[j].getValue().getBytes().length;
                    totalToSend += multipartBoundaryBits.length + dashdash.length + 3 * newline.length;
                }
                totalToSend += parts[i].getData().length;
            }
            // closing boundary marker
            totalToSend += multipartBoundaryBits.length + 2 * dashdash.length + 2 * newline.length;
            
            for (int i=0; i < parts.length && !interrupted; i++) {
                write(os, newline);
                write(os, dashdash);
                write(os, multipartBoundaryBits);
                write(os, newline);
                
                boolean wroteAtleastOneHeader = false;
                final Arg[] headers = parts[i].getHeaders();
                for (int j=0; j < headers.length; j++) {
                    write(os, (headers[j].getKey() + ": " + headers[j].getValue()).getBytes());
                    write(os, newline);
                    wroteAtleastOneHeader = true;
                }
                if (wroteAtleastOneHeader) {
                    write(os, newline);
                }
                
                write(os, parts[i].getData());
            }
            
            // closing boundary marker
            write(os, newline);
            write(os, dashdash);
            write(os, multipartBoundaryBits);
            write(os, dashdash);
            write(os, newline);
        } else if (inputArgs != null) {
            final byte[] argBytes = args.toString().getBytes();
            totalToSend = argBytes.length;
            write(os, argBytes);
        } else {
            throw new IOException("No data to POST - either input args or multipart must be non-null");
        }
    }
    
    private void readResponse(final HttpConnection conn,
        final Response response) throws IOException {
        
        totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
        
        final byte[] cbuf = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bos = null;
        InputStream is = null;
        try {
            is = conn.openInputStream();
            bos = new ByteArrayOutputStream();
            int nread = 0;
            while ((nread = is.read(cbuf)) > 0 && !interrupted) {
                bos.write(cbuf, 0, nread);
                received += nread;
                if (listener != null) {
                    try {
                        listener.readProgress(context, received, totalToReceive);
                    } catch (Throwable th) {
                    if (DEBUG) {
                        System.err.println("Uncaught throwable in listener: ");
                        th.printStackTrace();
                    }
                    }
                }
            }
        } finally {
            if (is != null) { is.close(); }
            if (bos != null) { bos.close(); }
        }
        
        if (interrupted) { return; }
        
        final String content = bos.toString().trim();
        response.result = Result.fromContent(content, response.contentType);
    }
    
    private void copyResponseHeaders(final HttpConnection conn,
        final Response response) throws IOException {
        
        // pass 1 - count the number of headers
        int headerCount = 0;
        for (int i=0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                headerCount++;
            }
        }
        
        response.headers = new Arg[headerCount];
        
        // pass 2 - now copy the headers
        for (int i=0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                response.headers[i] = new Arg(key, val);
            }
        }
    }
    
    private void processContentType(final HttpConnection conn,
        final Response response) throws IOException {
        
        response.contentType = conn.getHeaderField(Arg.CONTENT_TYPE);
        if (response.contentType == null) {
            // assume UTF-8 and XML if not specified
            response.contentType = Result.APPLICATION_XML_CONTENT_TYPE;
            response.charset = UTF8_CHARSET;
            return;
        }
        final int semi = response.contentType.indexOf(';');
        if (semi >= 0) {
            response.charset = response.contentType.substring(semi + 1).trim();
            final int eq = response.charset.indexOf('=');
            if (eq < 0) {
                throw new IOException("Missing charset value: " + response.charset);
            }
            response.charset = unquote(response.charset.substring(eq + 1).trim());
            response.contentType = response.contentType.substring(0, semi).trim();
        }
        if (response.charset != null && !UTF8_CHARSET.equals(response.charset.toLowerCase())) {
            throw new IOException("Unsupported charset: " + response.charset);
        }
    }
    
    private static String unquote(final String str) {
        if (str.startsWith("\"") && str.endsWith("\"") ||
            str.startsWith("'") && str.endsWith("'")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
    
    // TODO: verify correctness
    private static String encode(final String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        final byte[] buf = str.getBytes("utf-8");
        final StringBuffer sbuf = new StringBuffer(buf.length * 3);
        for (int i=0; i < buf.length; i++) {
            final byte ch = buf[i];
            if ((ch >= 'A' && ch <= 'Z') ||
                (ch >= 'a' && ch <= 'z') ||
                (ch >= '0' && ch <= '9') ||
                (ch == '-' || ch == '_' || ch == '.' || ch == '~')) {
                sbuf.append((char) ch);
            } else if (ch == ' ') {
                sbuf.append('+');
            } else {
                sbuf.append('%');
                sbuf.append(Integer.toHexString(ch & 0xff));
            }
        }
        return sbuf.toString();
    }
}
