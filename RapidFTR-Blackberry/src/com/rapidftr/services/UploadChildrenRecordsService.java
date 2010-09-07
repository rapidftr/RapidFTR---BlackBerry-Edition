package com.rapidftr.services;

import java.util.Vector;

import javax.microedition.io.HttpConnection;

import com.rapidftr.controllers.ControllerCallback;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class UploadChildrenRecordsService implements ControllerCallback {

	private HttpService httpService;
	private HttpRequestHandler listener;
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
			listener.markProcessComplete();
			return;
		}
		uploadChildRecordAtIndex();
	}

	private void uploadChildRecordAtIndex() {
		listener.updateRequestProgress(index,
				childrenList.size());
		Child child = (Child) childrenList.elementAt(index);
		PostData postData = child.getPostData();;
		Arg multiPart = new Arg("Content-Type",
				"multipart/form-data;boundary=" + postData.getBoundary());
		//Arg putRequest = new Arg("X-HTTP-Method-Override","PUT");
		Arg json = HttpUtility.HEADER_ACCEPT_JSON;
		Arg[] httpArgs = { multiPart, json };
		
		if (child.isNewChild()) {
			httpService.post("children", null, httpArgs, listener, postData, null);
		}else{
			Arg putRequest = new Arg("X-HTTP-Method-Override","PUT");
			Arg[] putHttpArgs = { multiPart, json,putRequest };
			httpService.post("children/"+child.getField("_id"), null, putHttpArgs, listener, postData, null);
		}

		if (index == childrenList.size() - 1) {
			listener.markProcessComplete();
			return;
		}
		index++;
		uploadChildRecordAtIndex();

	}

	
	public void cancelUploadOfChildRecords() {
		httpService.cancelRequest();
	}

	public void setListener(
			HttpRequestHandler listener) {
		this.listener = listener;
	}

	public void uploadChildRecord(Child child) {
		childrenList.addElement(child);
		index = 0;
		if (childrenList == null) {
			listener.markProcessComplete();
			return;
		}
		uploadChildRecordAtIndex();		
	}

	public void onRequestFailure(Exception exception) {
		// TODO Auto-generated method stub
		
	}

	public void onRequestSuccess(Object context, Response result) {
		Child child = (Child) childrenList.elementAt(index);
		try {
		child.setField("unique_identifier",result.getResult().getAsString("unique_identifier"));
		child.setField("_id",result.getResult().getAsString("_id"));
		child.setField("_rev",result.getResult().getAsString("_rev"));
		child.setField("histories",result.getResult().getAsString("histories"));
		child.clearEditHistory();
		childRecordStore.storeChildren(childrenList);
		if (index == childrenList.size() - 1) {
			listener.markProcessComplete();
			return;
		}
		} catch (ResultException e) {
			//FIX mE
			throw new RuntimeException();
		}

		index++;
		uploadChildRecordAtIndex();
		
	}

}
