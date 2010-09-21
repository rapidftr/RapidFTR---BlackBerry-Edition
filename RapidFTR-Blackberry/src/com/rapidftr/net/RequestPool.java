package com.rapidftr.net;

import com.sun.me.web.request.Request;

public class RequestPool implements Runnable {
	// a simple implementation of singleton
	private static RequestPool _instance = new RequestPool();

	static final int MAXIMUM_ACTIVE_THREADS = 5;

	int activeThreads = 0;

	WorkQueue requestQueue;

	public RequestPool() {
		requestQueue = new WorkQueue();
		new Thread(this).start();
	}

	public static RequestPool getInstance() {
		return _instance;
	}

	public void execute(Request request) {
		synchronized (requestQueue) {
			requestQueue.addRequest(new WorkerThread(request));
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

	public void cancelAllRequests() {
		requestQueue.cancelAllRequests();
	}

	private class WorkerThread implements Runnable {
		Request request;

		public WorkerThread(Request request) {
			super();
			this.request = request;
		}

		public void run() {
			try {
				request.run();
			} finally {
				decrementActiveThreadCount();
			}

		}

	}

}
