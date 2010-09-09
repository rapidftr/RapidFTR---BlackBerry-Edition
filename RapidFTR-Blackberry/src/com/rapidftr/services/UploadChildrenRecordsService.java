package com.rapidftr.services;

import java.util.Vector;

import javax.microedition.io.HttpConnection;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class UploadChildrenRecordsService implements RequestListener {

	private HttpService httpService;
	private UploadChildrenRecordsSeriviceListener childRecordsUploadSeriviceListener;
	private final ChildrenRecordStore childRecordStore;

	int index = 0;
	private Vector childrenList = new Vector();

	public UploadChildrenRecordsService(HttpService httpService,
			ChildrenRecordStore childRecordStore) {
		this.httpService = httpService;
		this.childRecordStore = childRecordStore;
	}

	public void uploadChildRecords() {

		childrenList = childRecordStore.getAllChildren();
		index = 0;
		if (childrenList == null) {
			childRecordsUploadSeriviceListener.onUploadComplete();
			return;
		}
		uploadChildRecordAtIndex();
	}

	private void uploadChildRecordAtIndex() {
		childRecordsUploadSeriviceListener.updateUploadStatus(index,
				childrenList.size());
		Child child = (Child) childrenList.elementAt(index);
		PostData postData = child.getPostData();;
		Arg multiPart = new Arg("Content-Type",
				"multipart/form-data;boundary=" + postData.getBoundary());
		//Arg putRequest = new Arg("X-HTTP-Method-Override","PUT");
		Arg json = HttpUtility.HEADER_ACCEPT_JSON;
		Arg[] httpArgs = { multiPart, json };
		
		if (child.isNewChild()) {
			httpService.post("children", null, httpArgs, this, postData, null);
		}else{
			Arg putRequest = new Arg("X-HTTP-Method-Override","PUT");
			Arg[] putHttpArgs = { multiPart, json,putRequest };
			httpService.post("children/"+child.getField("_id"), null, putHttpArgs, this, postData, null);
		}

		if (index == childrenList.size() - 1) {
			childRecordsUploadSeriviceListener.onUploadComplete();
			return;
		}
		index++;
		uploadChildRecordAtIndex();

	}

	public void done(Object context, Response result) throws Exception {

		if (result.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
			childRecordsUploadSeriviceListener.onAuthenticationFailure();
			return;
		}
		if (result.getException() != null) {
			childRecordsUploadSeriviceListener.onUploadFailed();
			return;
		}

		if (result.getCode() != HttpConnection.HTTP_OK) {
			childRecordsUploadSeriviceListener.onConnectionProblem();
			return;
		}

		Child child = (Child) childrenList.elementAt(index);
		child.setField("unique_identifier",result.getResult().getAsString("unique_identifier"));
		child.setField("_id",result.getResult().getAsString("_id"));
		child.setField("_rev",result.getResult().getAsString("_rev"));
		child.setField("histories",result.getResult().getAsString("histories"));
		child.clearEditHistory();
		childRecordStore.storeChildren(childrenList);
		if (index == childrenList.size() - 1) {
			childRecordsUploadSeriviceListener.onUploadComplete();
			return;
		}

		index++;
		uploadChildRecordAtIndex();

	}

	public void readProgress(Object context, int bytes, int total) {

	}

	public void writeProgress(Object context, int bytes, int total) {

	}

	public void cancelUploadOfChildRecords() {
		httpService.cancelRequest();
	}

	public void setListener(
			UploadChildrenRecordsSeriviceListener childRecordsUploadSeriviceListener) {
		this.childRecordsUploadSeriviceListener = childRecordsUploadSeriviceListener;
	}

	public void uploadChildRecord(Child child) {
		childrenList.addElement(child);
		index = 0;
		if (childrenList == null) {
			childRecordsUploadSeriviceListener.onUploadComplete();
			return;
		}
		uploadSingleChild(child);
	}

	private void uploadSingleChild(Child child)
	{
			childRecordsUploadSeriviceListener.updateUploadStatus(index,
					childrenList.size());
			
			PostData postData = child.getPostData();
			Arg multiPart = new Arg("Content-Type",
					"multipart/form-data;boundary=" + postData.getBoundary());
			//Arg putRequest = new Arg("X-HTTP-Method-Override","PUT");
			Arg json = HttpUtility.HEADER_ACCEPT_JSON;
			Arg[] httpArgs = { multiPart, json };
			
			if (child.isNewChild()) {
				httpService.post("children", null, httpArgs, this, postData, null);
			}else{
				Arg putRequest = new Arg("X-HTTP-Method-Override","PUT");
				Arg[] putHttpArgs = { multiPart, json,putRequest };
				httpService.post("children/"+child.getField("_id"), null, putHttpArgs, this, postData, null);
			}
		
	}
}
