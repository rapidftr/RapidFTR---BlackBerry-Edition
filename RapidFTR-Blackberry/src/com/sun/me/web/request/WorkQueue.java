package com.sun.me.web.request;

public class WorkQueue {

	private Thread[] threads;

	private int currentSize;
	private int front;
	private int back;

	private static final int INITIAL_CAPACITY = 10;

	public WorkQueue(RequestPool requestPool) {
		threads = new Thread[INITIAL_CAPACITY];
		makeEmpty();
	}

	public boolean isEmpty() {
		return currentSize == 0;
	}

	public void makeEmpty() {
		currentSize = 0;
		front = 0;
		back = -1;
	}

	public void spawnNextRequestThread() {
		if (!isEmpty()) {
			currentSize--;
			threads[front].start();
			front = increment(front);
		}
	}

	public void addRequest(Request request) {
		Thread workerThread = new Thread(request);
		if (currentSize == threads.length)
			doubleCapacity();
		back = increment(back);
		threads[back] = workerThread;
		currentSize++;
	}

	private int increment(int back) {
		if (++back == threads.length)
			back = 0;
		return back;
	}

	private void doubleCapacity() {
		Thread[] newArray;

		newArray = new Thread[threads.length * 2];

		for (int i = 0; i < currentSize; i++, front = increment(front))
			newArray[i] = threads[front];

		threads = newArray;
		front = 0;
		back = currentSize - 1;
	}

}
