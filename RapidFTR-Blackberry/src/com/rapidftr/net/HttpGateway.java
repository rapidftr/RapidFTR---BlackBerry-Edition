package com.rapidftr.net;

import com.sun.me.web.path.Result;
import com.sun.me.web.request.*;

import javax.microedition.io.HttpConnection;
import java.io.*;

public class HttpGateway {

    private static final int BUFFER_SIZE = 512;
    private static final boolean DEBUG = true;
    public static final String UTF8_CHARSET = "utf-8";    
    
    private ConnectionFactory connectionFactory;

    public HttpGateway(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Response perform(Request request) throws IOException {

        final StringBuffer args = new StringBuffer();
        final Arg[] inputArgs = request.getInputArgs();
        if (inputArgs != null) {
            if (inputArgs.length > 0) {
                for (int i = 0; i < inputArgs.length; i++) {
                    if (inputArgs[i] != null) {
                        args.append(encode(inputArgs[i].getKey()));
                        args.append("=");
                        args.append(encode(inputArgs[i].getValue()));
                        if (i + 1 < inputArgs.length && inputArgs[i + 1] != null) {
                            args.append("&");
                        }
                    }
                }
            }
        }

        final StringBuffer location = new StringBuffer(request.getUrl());
        final String method = request.getMethod();
        if (HttpConnection.GET.equals(method) && args.length() > 0) {
            location.append("?");
            location.append(args.toString());
        }

        HttpConnection conn = null;
        try {
            conn = connectionFactory.openConnection(location.toString());
            conn.setRequestMethod(method);
            Arg[] httpArgs = request.getHttpArgs();
            if (httpArgs != null) {
                for (int i = 0; i < httpArgs.length; i++) {
                    if (httpArgs[i] != null) {
                        final String value = httpArgs[i].getValue();
                        if (value != null) {
                            conn.setRequestProperty(httpArgs[i].getKey(), value);
                        }
                    }
                }
            }

            if (HttpConnection.POST.equals(method)) {
                OutputStream os = null;
                try {
                    os = conn.openOutputStream();
                    writePostData(request, args, os);
                } finally {
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException ignore) {
                        }
                    }
                }
            }

            Response response = new Response();
            copyResponseHeaders(conn, response);

            response.setResponseCode(conn.getResponseCode());
            if (response.getResponseCode() != HttpConnection.HTTP_OK) {

            }

            processContentType(conn, response);
            readResponse(conn, response, request.getListener(), request.getContext());
            return response;
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private void writePostData(final Request request, final StringBuffer args, final OutputStream os) throws IOException {
        int totalToSend = 0;
        PostData multiPart = request.getPostData();
        RequestListener listener = request.getListener();
        Object context = request.getContext();
        if (multiPart != null) {
            final byte[] multipartBoundaryBits = multiPart.getBoundary().getBytes();
            final byte[] newline = "\r\n".getBytes();
            final byte[] dashdash = "--".getBytes();

            // estimate totalBytesToSend
            final Part[] parts = multiPart.getParts();
            for (int i = 0; i < parts.length; i++) {
                final Arg[] headers = parts[i].getHeaders();
                for (int j = 0; j < headers.length; j++) {
                    totalToSend += headers[j].getKey().getBytes().length;
                    totalToSend += headers[j].getValue().getBytes().length;
                    totalToSend += multipartBoundaryBits.length + dashdash.length + 3 * newline.length;
                }
                totalToSend += parts[i].getData().length;
            }
            // closing boundary marker
            totalToSend += multipartBoundaryBits.length + 2 * dashdash.length + 2 * newline.length;

            for (int i = 0; i < parts.length; i++) {

                write(os, dashdash, totalToSend, listener, context);
                write(os, multipartBoundaryBits, totalToSend, listener, context);
                write(os, newline, totalToSend, listener, context);

                boolean wroteAtleastOneHeader = false;
                final Arg[] headers = parts[i].getHeaders();
                for (int j = 0; j < headers.length; j++) {
                    write(os, (headers[j].getKey() + ": " + headers[j].getValue()).getBytes(), totalToSend, listener, context);
                    write(os, newline, totalToSend, listener, context);
                    wroteAtleastOneHeader = true;
                }
                if (wroteAtleastOneHeader) {
                    write(os, newline, totalToSend, listener, context);
                }

                write(os, parts[i].getData(), totalToSend, listener, context);
                write(os, newline, totalToSend, listener, context);
            }

            // closing boundary marker
            // write(os, newline);
            write(os, dashdash, totalToSend, listener, context);
            write(os, multipartBoundaryBits, totalToSend, listener, context);
            write(os, dashdash, totalToSend, listener, context);
            write(os, newline, totalToSend, listener, context);
        } else if (args != null) {
            final byte[] argBytes = args.toString().getBytes();
            totalToSend = argBytes.length;
            write(os, argBytes, totalToSend, listener, context);
        } else {
            throw new IOException("No data to POST - either input args or multipart must be non-null");
        }
    }

    // data may be large, send in chunks while reporting progress and checking for interruption
    private void write(final OutputStream os, final byte[] data, int totalToSend,
                       RequestListener listener, Object context) throws IOException {
        int sent = 0;
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
            } while (length > 0);
        }
    }

    private void processContentType(final HttpConnection conn, final Response response) throws IOException {

        response.setContentType(conn.getHeaderField(Arg.CONTENT_TYPE));
        if (response.getContentType() == null) {
            // assume UTF-8 and XML if not specified
            response.setContentType(Result.APPLICATION_XML_CONTENT_TYPE);
            response.setCharset(UTF8_CHARSET);
            return;
        }
        final int semi = response.getContentType().indexOf(';');
        if (semi >= 0) {
            response.setCharset(response.getContentType().substring(semi + 1).trim());
            final int eq = response.getCharset().indexOf('=');
            if (eq < 0) {
                throw new IOException("Missing charset value: " + response.getCharset());
            }
            response.setCharset(unquote(response.getCharset().substring(eq + 1).trim()));
            response.setContentType(response.getContentType().substring(0, semi).trim());
        }
        if (response.getCharset() != null && !UTF8_CHARSET.equals(response.getCharset().toLowerCase())) {
            throw new IOException("Unsupported charset: " + response.getCharset());
        }
    }

    private static String unquote(final String str) {
		if (str.startsWith("\"") && str.endsWith("\"") || str.startsWith("'") && str.endsWith("'")) { return str.substring(1, str.length() - 1); }
		return str;
	}
    
    private void readResponse(final HttpConnection conn, final Response response, RequestListener listener, Object context) throws IOException {

        int totalToReceive = conn.getHeaderFieldInt(Arg.CONTENT_LENGTH, 0);
        int received = 0;
        
        final byte[] cbuf = new byte[BUFFER_SIZE];
        ByteArrayOutputStream bos = null;
        InputStream is = null;
        try {
            is = conn.openInputStream();
            bos = new ByteArrayOutputStream();
            int nread = 0;
            while ((nread = is.read(cbuf)) > 0) {
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
            if (is != null) {
                is.close();
            }
            if (bos != null) {
                bos.close();
            }
        }

        if (response.getContentType().startsWith(Result.IMAGE_CONTENT_TYPE_PATTERN)) {
            response.setResult(Result.fromContent(bos.toByteArray(), response.getContentType()));
        } else {
            response.setResult(Result.fromContent(bos.toString().trim(), response.getContentType()));
        }
    }

    private void copyResponseHeaders(final HttpConnection conn, final Response response) throws IOException {

        // pass 1 - count the number of headers
        int headerCount = 0;
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                headerCount++;
            }
        }

        Arg[] headers = new Arg[headerCount];

        // pass 2 - now copy the headers
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            final String key = conn.getHeaderFieldKey(i);
            final String val = conn.getHeaderField(i);
            if (key == null || val == null) {
                break;
            } else {
                headers[i] = new Arg(key, val);
            }
        }
        response.setHeaders(headers);
    }


    // TODO: verify correctness
    private static String encode(final String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        }
        final byte[] buf = str.getBytes("utf-8");
        final StringBuffer sbuf = new StringBuffer(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            final byte ch = buf[i];
            if ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || (ch >= '0' && ch <= '9') || (ch == '-' || ch == '_' || ch == '.' || ch == '~')) {
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
