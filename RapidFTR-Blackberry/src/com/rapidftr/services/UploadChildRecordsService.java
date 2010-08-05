package com.rapidftr.services;

import java.util.Vector;

import javax.microedition.io.HttpConnection;

import net.rim.blackberry.api.browser.MultipartPostData;
import net.rim.blackberry.api.mail.Multipart;

import com.rapidftr.datastore.ChildRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpService;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class UploadChildRecordsService implements RequestListener {

	private HttpService httpService;
	private UploadChildRecordsSeriviceListener childRecordsUploadSeriviceListener;
	private final ChildRecordStore childRecordStore;

	public UploadChildRecordsService(HttpService httpService, ChildRecordStore childRecordStore) {
		this.httpService = httpService;
		this.childRecordStore = childRecordStore;
	}

	public void uploadChildRecords() {

		Vector childrenList = childRecordStore.getAllChildren();
		
		Child child = (Child) childrenList.elementAt(0);
		
		String boundary = "abced";
		Part[] parts = child.getPostData();
		PostData postData = new PostData(parts, boundary );
		System.out.println(child.toString());
		httpService.postWithAuthorizationToken("children",postData,this);
	}

	public void done(Object context, Response result) throws Exception {

		if (result.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
			childRecordsUploadSeriviceListener.onAuthenticationFailure();
			return;
		}
		if (result.getException() != null) {
			System.out.println("Exception:"+result.getException().toString());
			childRecordsUploadSeriviceListener.onUploadFailed();
			return;
		}

		if (result.getCode() != HttpConnection.HTTP_OK) {
			childRecordsUploadSeriviceListener.onConnectionProblem();
			return;
		}

		childRecordsUploadSeriviceListener.onUploadComplete();

	}

	public void readProgress(Object context, int bytes, int total) {
		
	}

	public void writeProgress(Object context, int bytes, int total) {
		//System.out.println(bytes+":"+total);
		childRecordsUploadSeriviceListener.updateUploadStatus(bytes, total);
	}

	public void cancelUploadOfChildRecords() {
		httpService.cancelRequest();
	}

	public void setListener(
			UploadChildRecordsSeriviceListener childRecordsUploadSeriviceListener) {
		this.childRecordsUploadSeriviceListener = childRecordsUploadSeriviceListener;

	}

}
