package com.sun.me.web.request;

public class RequestPool implements Runnable {
	// a simple implementation of singleton
	private static RequestPool _instance = new RequestPool();

	private static final int MAXIMUM_ACTIVE_THREADS = 5;

	private int activeThreads = 0;

	WorkQueue requestQueue;

	private RequestPool() {
		requestQueue = new WorkQueue(this);
		new Thread(this).start();
	}

	public static RequestPool getInstance() {
		return _instance;
	}

	public void execute(Request request) {
		synchronized (requestQueue) {
			requestQueue.addRequest(request);
			requestQueue.notify();
		}
	}

	public void run() {
		while (true) {
			if (!requestQueue.isEmpty()
					&& activeThreads < MAXIMUM_ACTIVE_THREADS) {
				activeThreads++;
				requestQueue.spawnNextRequestThread();
			} else {
				if (requestQueue.isEmpty()) {
					synchronized (requestQueue) {
						try {
							requestQueue.wait();
						} catch (InterruptedException ignored) {
						}
					}
				}
			}
		}
	}

	public synchronized void decrementActiveThreadCount() {
		activeThreads--;
		synchronized (requestQueue) {
			requestQueue.notify();
		}
	
	}

}
