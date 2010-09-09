package com.rapidftr.services;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Response;

public class ChildSyncService extends RequestAwareService {

	private final ChildrenRecordStore childRecordStore;

	public ChildSyncService(HttpService httpService,
			ChildrenRecordStore childRecordStore) {
		super(httpService);
		this.childRecordStore = childRecordStore;
	}

	public void uploadChildRecords() {
		uploadChildren( childRecordStore.getAllChildren());
	}

	private void uploadChildren(Vector childrenList) {
		requestHandler.setRequestInProgress();
		requestHandler.getRequestCallBack().updateProgressMessage("Uploading Records");
		Enumeration children = childrenList.elements();
		while (children.hasMoreElements()) {
			Child child = (Child) children.nextElement();
			PostData postData = child.getPostData();
			Arg multiPart = new Arg("Content-Type",
					"multipart/form-data;boundary=" + postData.getBoundary());
			Arg json = HttpUtility.HEADER_ACCEPT_JSON;
			Arg[] httpArgs = { multiPart, json };
			if (child.isNewChild()) {
				requestHandler.incrementActiveRequests(1);
				httpService.post("children", null, httpArgs, requestHandler,
						postData, null);
			} else if(child.isUpdated()){
				requestHandler.incrementActiveRequests(1);
				httpService.put("children/" + child.getField("_id"), null,
						httpArgs, requestHandler, postData, null);
			}
		}
	}

	

	public void uploadChildRecord(Child child) {

		Vector childrenList = new Vector();
		childrenList.addElement(child);
		uploadChildren(childrenList);
	}

	public void onRequestSuccess(Object context, Response result) {
		// sync child with local store
		Child child = new Child();
		try {
			JSONObject jsonChild = new JSONObject(result.getResult().toString());
			HttpServer.printResponse(result);
			JSONArray fieldNames = jsonChild.names();
			for (int j = 0; j < fieldNames.length(); j++) {
				String fieldName = fieldNames.get(j).toString();
				String fieldValue = jsonChild.getString(fieldName);
				child.setField(fieldName, fieldValue);
			}
			child.clearEditHistory();
			childRecordStore.addOrUpdateChild(child);

		} catch (JSONException e) {
			//e.printStackTrace();
		}
		

	}

	public void syncAllChildRecords() throws ServiceException {
		new Thread(){public void run() {requestHandler.setRequestInProgress();
		uploadChildRecords();
	
		try {
			downloadNewChildRecords();
		} catch (IOException e) {
			requestHandler.markProcessFailed();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			requestHandler.markProcessFailed();
	}
		if(requestHandler.isProcessCompleted()){
			requestHandler.markProcessComplete();
		}};}.start();
		
	
	}

	private void downloadNewChildRecords() throws IOException, JSONException {
		requestHandler.getRequestCallBack().updateProgressMessage("Downloading Records");
		Vector childNeedToDownload = childRecordsNeedToBeDownload();
		Enumeration items = childNeedToDownload.elements();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		while (items.hasMoreElements()) {
			requestHandler.incrementActiveRequests(1);
			httpService.get("children/" + items
					.nextElement().toString(), null, httpArgs, requestHandler);
    	}

	}

	private Vector childRecordsNeedToBeDownload() throws IOException, JSONException {
		Vector childNeedToDownload = new Vector();
		Hashtable offlineIdRevXREF = getOfflineStoredChildrenIdRevMapping();
		Hashtable onlineIdRevXREF;

			onlineIdRevXREF = getOnlineStoredChildrenIdRevMapping();
		

		Enumeration items = onlineIdRevXREF.keys();

		while (items.hasMoreElements()) {
			String key = (String) items.nextElement();
			if ((!offlineIdRevXREF.containsKey(key))
					|| (offlineIdRevXREF.containsKey(key) && !offlineIdRevXREF
							.get(key).equals(onlineIdRevXREF.get(key)))) {
				childNeedToDownload.addElement(key);
			}
		}
		return childNeedToDownload;
	}

	public Vector getAllChildrenFromOnlineStore() throws IOException,
			JSONException {

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		Response response = httpService.get("children", null, httpArgs);
		Result result = response.getResult();
		HttpServer.printResponse(response);
		JSONArray jsonChildren = result.getAsArray("");
		Vector children = new Vector();
		for (int i = 0; i < jsonChildren.length(); i++) {
			JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
			Child child = new Child();
			JSONArray fieldNames = jsonChild.names();
			for (int j = 0; j < fieldNames.length(); j++) {
				String fieldName = fieldNames.get(j).toString();
				String fieldValue = jsonChild.getString(fieldName);
				child.setField(fieldName, fieldValue);
			}

			children.addElement(child);
		}
		return children;

	}

	private Hashtable getOnlineStoredChildrenIdRevMapping() throws IOException, JSONException {

		Hashtable mapping = new Hashtable();

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		Response response = httpService.get("children-ids", null, httpArgs);
		Result result = response.getResult();
		HttpServer.printResponse(response);
			JSONArray jsonChildren = result.getAsArray("");
			for (int i = 0; i < jsonChildren.length(); i++) {
				JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
				mapping.put(jsonChild.getString("id"), jsonChild
						.getString("rev"));
			}
		
		return mapping;
	}

	private Hashtable getOfflineStoredChildrenIdRevMapping() {
		Hashtable mapping = new Hashtable();
		Enumeration items = childRecordStore.getAllChildren().elements();

		while (items.hasMoreElements()) {
			Child child = (Child) items.nextElement();
			Object id = child.getField("_id");
			Object rev = child.getField("_rev");
			if (id != null && rev != null) {
				mapping.put(id.toString(), rev.toString());
			}
		}
		return mapping;
	}


	public Child getChildFromOnlineStore(String id) throws IOException {
		Child child = new Child();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		Response response = httpService.get("children/" + id, null, httpArgs);
		Result result = response.getResult();
		HttpServer.printResponse(response);
		try {
			JSONObject jsonChild = new JSONObject(result.toString());
			JSONArray fieldNames = jsonChild.names();
			for (int j = 0; j < fieldNames.length(); j++) {
				String fieldName = fieldNames.get(j).toString();
				String fieldValue = jsonChild.getString(fieldName);
				child.setField(fieldName, fieldValue);
			}
		} catch (JSONException e) {
			throw new ServiceException(
					"JSON returned from get children is in unexpected format");
		}
		return child;
	}

}
