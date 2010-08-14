package com.rapidftr.services;

import com.rapidftr.net.HttpServiceListener;

public interface FormServiceListener extends HttpServiceListener {

	void updateDownloadStatus(int received, int total);

	void onDownloadComplete(String formsAsJson);

	void onDownloadFailed();

}
