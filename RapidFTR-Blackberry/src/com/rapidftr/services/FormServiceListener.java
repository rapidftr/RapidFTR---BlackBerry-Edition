package com.rapidftr.services;

import java.util.Vector;

import com.rapidftr.net.HttpServiceListener;

public interface FormServiceListener extends HttpServiceListener {

	void updateDownloadStatus(int received, int total);

	void onDownloadComplete(Vector forms);

	void downloadFailed();

}
