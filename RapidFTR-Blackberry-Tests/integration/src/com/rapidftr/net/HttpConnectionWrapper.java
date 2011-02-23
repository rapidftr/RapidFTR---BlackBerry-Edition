package com.rapidftr.net;

import javax.microedition.io.HttpConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLConnection;

public class HttpConnectionWrapper implements HttpConnection {
    private HttpURLConnection urlConnection;
    
    public HttpConnectionWrapper(HttpURLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    public String getURL() {
        return null;
    }

    public String getProtocol() {
        return null;
    }

    public String getHost() {
        return null;
    }

    public String getFile() {
        return null;
    }

    public String getRef() {
        return null;
    }

    public String getQuery() {
        return null;
    }

    public int getPort() {
        return 0;
    }

    public String getRequestMethod() {
        return null;
    }

    public void setRequestMethod(String s) throws IOException {
        urlConnection.setRequestMethod(s);
    }

    public String getRequestProperty(String s) {
        return null;
    }

    public void setRequestProperty(String key, String value) throws IOException {
        urlConnection.setRequestProperty(key, value);
    }

    public int getResponseCode() throws IOException {
        return urlConnection.getResponseCode();
    }

    public String getResponseMessage() throws IOException {
        return null;
    }

    public long getExpiration() throws IOException {
        return 0;
    }

    public long getDate() throws IOException {
        return 0;
    }

    public long getLastModified() throws IOException {
        return 0;
    }

    public String getHeaderField(String s) throws IOException {
        return urlConnection.getHeaderField(s);
    }

    public int getHeaderFieldInt(String s, int i) throws IOException {
        return urlConnection.getHeaderFieldInt(s, i);
    }

    public long getHeaderFieldDate(String s, long l) throws IOException {
        return 0;
    }

    public String getHeaderField(int i) throws IOException {
        return urlConnection.getHeaderField(i);
    }

    public String getHeaderFieldKey(int i) throws IOException {
        return urlConnection.getHeaderFieldKey(i);
    }

    public String getType() {
        return null;
    }

    public String getEncoding() {
        return null;
    }

    public long getLength() {
        return 0;
    }

    public InputStream openInputStream() throws IOException {
        return urlConnection.getInputStream();
    }

    public DataInputStream openDataInputStream() throws IOException {
        return null;
    }

    public OutputStream openOutputStream() throws IOException {
        return urlConnection.getOutputStream();
    }

    public DataOutputStream openDataOutputStream() throws IOException {
        return null;
    }

    public void close() throws IOException {
        
    }
}
