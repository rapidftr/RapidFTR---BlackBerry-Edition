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
import com.rapidftr.net.ControllerCallback;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Response;

public class ChildSyncService implements ControllerCallback {

	private HttpService httpService;
	private HttpRequestHandler listener;
	private final ChildrenRecordStore childRecordStore;

	int index = 0;
	private Vector childrenList = new Vector();

	public ChildSyncService(HttpService httpService,
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
		listener.updateRequestProgress(index, childrenList.size());
		Child child = (Child) childrenList.elementAt(index);
		PostData postData = child.getPostData();

		Arg multiPart = new Arg("Content-Type", "multipart/form-data;boundary="
				+ postData.getBoundary());
		Arg json = HttpUtility.HEADER_ACCEPT_JSON;
		Arg[] httpArgs = { multiPart, json };

		if (child.isNewChild()) {
			httpService.post("children", null, httpArgs, listener, postData,
					null);
		} else {
			Arg putRequest = new Arg("X-HTTP-Method-Override", "PUT");
			Arg[] putHttpArgs = { multiPart, json, putRequest };
			httpService.post("children/" + child.getField("_id"), null,
					putHttpArgs, listener, postData, null);
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

	public void setListener(HttpRequestHandler listener) {
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
			child.setField("unique_identifier", result.getResult().getAsString(
					"unique_identifier"));
			child.setField("_id", result.getResult().getAsString("_id"));
			child.setField("_rev", result.getResult().getAsString("_rev"));
			child.setField("histories", result.getResult().getAsString(
					"histories"));
			child.clearEditHistory();
			childRecordStore.storeChildren(childrenList);
			if (index == childrenList.size() - 1) {
				listener.markProcessComplete();
				return;
			}
		} catch (ResultException e) {
			// FIXME
			throw new RuntimeException();
		}

		index++;
		uploadChildRecordAtIndex();

	}

	public void syncAllChildRecords() throws ServiceException {
		 uploadChildRecords();
		try {
			downloadNewChildRecords();
		} catch (IOException e) {
			throw new ServiceException(
					"Error occured while getting records fromserver");
		}
	}

	private void downloadNewChildRecords() throws IOException {
		Vector childNeedToDownload = childRecordsNeedToBeDownload();

		Enumeration items = childNeedToDownload.elements();

		while (items.hasMoreElements()) {
			childRecordStore.addOrUpdateChild(getChildFromOnlineStore(items
					.nextElement().toString()));
		}
	}

	private Vector childRecordsNeedToBeDownload() {
		Vector childNeedToDownload = new Vector();
		Hashtable offlineIdRevXREF = getOfflineStoredChildrenIdRevMapping();
		Hashtable onlineIdRevXREF;
		try {
			onlineIdRevXREF = getOnlineStoredChildrenIdRevMapping();
		} catch (IOException e) {
			throw new ServiceException(
					"Error occured while connecting to the server");
		}

		Enumeration items = onlineIdRevXREF.keys();

		while (items.hasMoreElements()) {
			String key = (String) items.nextElement();
			if ((!offlineIdRevXREF.contains(key))
					|| (offlineIdRevXREF.contains(key) && !offlineIdRevXREF
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

	private Hashtable getOnlineStoredChildrenIdRevMapping() throws IOException {

		Hashtable mapping = new Hashtable();

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		Response response = httpService.get("children-ids", null, httpArgs);
		Result result = response.getResult();
		HttpServer.printResponse(response);
		try {
			JSONArray jsonChildren = result.getAsArray("");
			for (int i = 0; i < jsonChildren.length(); i++) {
				JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
				mapping.put(jsonChild.getString("id"), jsonChild
						.getString("rev"));
			}
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from get children-ids is in unexpected format");
		} catch (JSONException e) {
			throw new ServiceException(
					"JSON returned from get children-ids is in unexpected format");
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

	// TODO check how to make this request async
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
