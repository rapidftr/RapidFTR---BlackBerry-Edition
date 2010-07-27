package com.rapidftr.services;

import java.util.Vector;

import org.json.me.JSONArray;

import com.rapidftr.net.HttpServiceListener;

public interface FormServiceListener extends HttpServiceListener {

	void updateDownloadStatus(int received, int total);

	void onDownloadComplete(String formsAsJson);

	void onDownloadFailed();

}
