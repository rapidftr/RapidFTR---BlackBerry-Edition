package com.rapidftr.services;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

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

	private static final String FILE_STORE_HOME_USER = "file:///store/home/user";

	private static final String PROCESS_STATE = "process_state";

	private static final String CHILD_TO_SYNC = "childToSync";

	private final ChildrenRecordStore childRecordStore;

	public ChildSyncService(HttpService httpService,
			ChildrenRecordStore childRecordStore) {
		super(httpService);
		this.childRecordStore = childRecordStore;
	}

	private void uploadChildRecords() {
		uploadChildren(childRecordStore.getAllChildren());
	}

	private void uploadChildren(Vector childrenList) {
		Enumeration children = childrenList.elements();
		int index = 0;
		while (children.hasMoreElements()) {
			Hashtable context = new Hashtable();
			Child child = (Child) children.nextElement();
			PostData postData = child.getPostData();
			context.put(PROCESS_STATE, "Uploading [" + (++index) + "/"
					+ childrenList.size() + "]");
			context.put(CHILD_TO_SYNC, child);
			Arg multiPart = new Arg("Content-Type",
					"multipart/form-data;boundary=" + postData.getBoundary());
			Arg json = HttpUtility.HEADER_ACCEPT_JSON;
			Arg[] httpArgs = { multiPart, json };
			if (child.isNewChild()) {
				requestHandler.post("children", null, httpArgs, postData,
						context);
			} else if (child.isUpdated()) {
				requestHandler.put("children/" + child.getField("_id"), null,
						httpArgs, postData, context);
			}
		}
	}

	public void syncChildRecord(Child child) {
		Vector childrenList = new Vector();
		childrenList.addElement(child);
		requestHandler.startNewProcess();
		uploadChildren(childrenList);
	}

	public void syncAllChildRecords() throws ServiceException {
		new Thread() {
			public void run() {
				requestHandler.getRequestCallBack().updateProgressMessage(
						"Syncing");
				requestHandler.startNewProcess();
				uploadChildRecords();
				downloadNewChildRecords();
              requestHandler.checkForProcessCompletion();
				// requestHandler.checkAndMarkProcessComplete();
			};
		}.start();

	}

	private void downloadNewChildRecords() {
		Vector childNeedToDownload = childRecordsNeedToBeDownload();
		Enumeration items = childNeedToDownload.elements();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		int index = 0;
		while (items.hasMoreElements()) {
			index++;
			Hashtable context = new Hashtable();
			context.put(PROCESS_STATE, "Downloading [" + index + "/"
					+ childNeedToDownload.size() + "]");
			Child child = new Child();
			String childId = items.nextElement().toString();
			child.setField("_id", childId);
			child.setField("name", childId);
			child.setField("last_known_location", "NA");
			context.put(CHILD_TO_SYNC, child);
			requestHandler.get("children/" + childId, null, httpArgs, context);
		}

	}

	private Vector childRecordsNeedToBeDownload() {
		Vector childNeedToDownload = new Vector();
		Hashtable offlineIdRevXREF = getOfflineStoredChildrenIdRevMapping();
		Hashtable onlineIdRevXREF = getOnlineStoredChildrenIdRevMapping();

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

	private Hashtable getOnlineStoredChildrenIdRevMapping() {

		Hashtable mapping = new Hashtable();

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		Response response;
		try {
			response = requestHandler.get("children-ids", null, httpArgs);

			if (response != null) {
				Result result = response.getResult();
				HttpServer.printResponse(response);
				JSONArray jsonChildren;

				jsonChildren = result.getAsArray("");

				for (int i = 0; i < jsonChildren.length(); i++) {
					JSONObject jsonChild = (JSONObject) jsonChildren.get(i);

					mapping.put(jsonChild.getString("id"), jsonChild
							.getString("rev"));
				}
			}
		} catch (ResultException e) {
			requestHandler.markProcessFailed("Sync failed due to"
					+ e.getMessage() + ". ");
		} catch (JSONException e) {
			requestHandler.markProcessFailed("Sync failed due to "
					+ e.getMessage() + ". ");
		} catch (IOException e) {
			requestHandler.markProcessFailed("Sync failed due to "
					+ e.getMessage() + ". ");
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

	public void clearState() {
		childRecordStore.deleteAllChildren();

	}

	public void onRequestSuccess(Object context, Response result) {
		requestHandler.getRequestCallBack().updateProgressMessage(
				((Hashtable) context).get(PROCESS_STATE).toString());
		// sync child with local store
		Child child = (Child) (((Hashtable) context).get(CHILD_TO_SYNC));
		try {
			JSONObject jsonChild = new JSONObject(result.getResult().toString());
			HttpServer.printResponse(result);
			JSONArray fieldNames = jsonChild.names();
			for (int j = 0; j < fieldNames.length(); j++) {
				String fieldName = fieldNames.get(j).toString();
				String fieldValue = jsonChild.getString(fieldName);
				child.setField(fieldName, fieldValue);
			}
			updateChildPhoto(child);
			child.syncSuccess();

		} catch (Exception e) {
			child.syncFailed(e.getMessage());
		} finally {
			childRecordStore.addOrUpdateChild(child);
		}

	}

	private void updateChildPhoto(Child child) {
		try {
			Arg[] httpArgs = new Arg[1];
			httpArgs[0] = HttpUtility.HEADER_CONTENT_TYPE_IMAGE;
			Response response = requestHandler.get("children/"
					+ child.getField("_id") + "/thumbnail", null, httpArgs);
			byte[] data = response.getResult().getData();

			String storePath = "";
			try {
				String sdCardPath = "file:///SDCard/Blackberry";
				FileConnection fc = (FileConnection) Connector.open(sdCardPath);
				if (fc.exists())
					storePath = sdCardPath;
				else
					storePath = FILE_STORE_HOME_USER;
			} catch (IOException ex) {
				storePath = FILE_STORE_HOME_USER;
			}

			String imagePath = storePath + "/pictures/"
					+ (String) child.getField("current_photo_key") + ".jpg";
			FileConnection fc = (FileConnection) Connector.open(imagePath);
			if (!fc.exists()) {
				fc.create(); // create the file if it doesn't exist
			}
			fc.setWritable(true);
			OutputStream outStream = fc.openOutputStream();
			outStream.write(data);
			outStream.close();
			fc.close();

			child.setField("current_photo_key", imagePath);
		} catch (IOException e) {
			child.syncFailed(e.getMessage());
		}
	}

	public void onRequestFailure(Object context, Exception exception) {
		requestHandler.getRequestCallBack().updateProgressMessage(
				((Hashtable) context).get(PROCESS_STATE).toString()
						+ " Failed. ");
		Child child = (Child) (((Hashtable) context).get(CHILD_TO_SYNC));
		child.syncFailed(exception.getMessage());
		childRecordStore.addOrUpdateChild(child);
	}

}
