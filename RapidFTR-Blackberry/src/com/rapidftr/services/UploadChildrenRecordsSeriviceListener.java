package com.rapidftr.services;

import com.rapidftr.net.HttpServiceListener;

public interface UploadChildrenRecordsSeriviceListener extends HttpServiceListener{

	void onUploadComplete();

	void onUploadFailed();

	void updateUploadStatus(int bytes, int total);

	

}
