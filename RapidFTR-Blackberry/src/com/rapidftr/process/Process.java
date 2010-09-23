package com.rapidftr.process;

public interface Process {
	public String name();
	public void startProcess();
	public void stopProcess();
	public boolean isCanceled();
}
