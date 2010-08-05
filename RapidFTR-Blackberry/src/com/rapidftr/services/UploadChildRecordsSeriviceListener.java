package com.rapidftr.services;

import com.rapidftr.net.HttpServiceListener;

public interface UploadChildRecordsSeriviceListener extends HttpServiceListener{

	void onUploadComplete();

	void onUploadFailed();

	void updateUploadStatus(int bytes, int total);

	

}
