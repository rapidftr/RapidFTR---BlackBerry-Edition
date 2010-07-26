package com.sun.me.web.request;

import java.io.IOException;
import java.io.InputStream;

public class ProgressInputStream extends InputStream {
    
    private static final boolean DEBUG = true;
    
    final InputStream is;
    final int total;
    final ProgressListener listener;
    final Object context;
    final int notifyInterval;
    
    int nread;
    
    public ProgressInputStream(final InputStream is, final int total,
        final ProgressListener listener, final Object context,
        final int notifyInterval) {
        
        this.is = is;
        this.total = total;
        this.listener = listener;
        this.context = context;
        this.notifyInterval = notifyInterval;
        
        nread = 0;
    }
    
    public int read() throws IOException {
        if ((++nread % notifyInterval) == 0) {
            try {
                listener.readProgress(context, nread, total);
            } catch (Throwable th) {
                if (DEBUG) {
                    System.err.println("Uncaught throwable in listener: ");
                    th.printStackTrace();
                }
            }
        }
        return is.read();
    }
    
    public void close() throws IOException {
        is.close();
        super.close();
    }
}
