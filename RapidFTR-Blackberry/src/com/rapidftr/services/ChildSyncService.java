package com.rapidftr.services;

import com.rapidftr.datastore.ChildAction;
import com.rapidftr.datastore.Children;
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
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ChildSyncService extends RequestAwareService {

	static final String PROCESS_STATE = "process_state";

	static final String CHILD_TO_SYNC = "childToSync";

	private final ChildrenRecordStore childRecordStore;
    private ChildPhotoUpdater childPhotoUpdater;

    public ChildSyncService(HttpService httpService,
			ChildrenRecordStore childRecordStore) {
        this(httpService, childRecordStore, new ChildPhotoUpdater());
    }

	public ChildSyncService(HttpService httpService,
			ChildrenRecordStore childRecordStore, ChildPhotoUpdater photoUpdater) {
		super(httpService);
		this.childRecordStore = childRecordStore;
        this.childPhotoUpdater = photoUpdater;
	}

	private void uploadChildRecords() {
		uploadChildren(childRecordStore.getAll());
	}

	private void uploadChildren(final Children children) {

		children.forEachChild(new ChildAction() {
			int index = 0;

			public void execute(Child child) {
				Hashtable context = new Hashtable();
				PostData postData = child.getPostData();
				context.put(PROCESS_STATE, "Uploading [" + (++index) + "/"
						+ children.count() + "]");
				context.put(CHILD_TO_SYNC, child);
				Arg multiPart = new Arg("Content-Type",
						"multipart/form-data;boundary="
								+ postData.getBoundary());
				Arg json = HttpUtility.HEADER_ACCEPT_JSON;
				Arg[] httpArgs = { multiPart, json };
				if (child.isNewChild()) {
					requestHandler.post("children", null, httpArgs, postData,
							context);
				} else if (child.isUpdated()) {
					requestHandler.put("children/" + child.getField("_id"),
							null, httpArgs, postData, context);
				}

			}
		});

	}

	public void syncChildRecord(Child child) {
		Vector childrenList = new Vector();
		childrenList.addElement(child);
		requestHandler.startNewProcess();
		uploadChildren(new Children(childrenList));
        requestHandler.checkForProcessCompletion();
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
			};
		}.start();

	}

	private void downloadNewChildRecords() {
		Vector childNeedToDownload = childRecordsNeedToBeDownload();
		Enumeration items = childNeedToDownload.elements();
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
			requestHandler.get("children/" + childId, null, HttpUtility
					.makeJSONHeader(), context);
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

		Response response;
		try {
			response = requestHandler.get("children-ids", null, HttpUtility
					.makeJSONHeader());

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
            sendMsgToHandlerWhenProcessFailed("There was an exception in the result.");
        } catch (JSONException e) {
            sendMsgToHandlerWhenProcessFailed("The data format is not correct.");
		} catch (IOException ignore) {
		}

		return mapping;
	}

    private void sendMsgToHandlerWhenProcessFailed(String msg) {
        requestHandler.markProcessFailed(msg);
    }

    private Hashtable getOfflineStoredChildrenIdRevMapping() {
		final Hashtable mapping = new Hashtable();
		childRecordStore.getAll().forEachChild(new ChildAction() {

			public void execute(Child child) {
				Object id = child.getField("_id");
				Object rev = child.getField("_rev");
				if (id != null && rev != null) {
					mapping.put(id.toString(), rev.toString());
				}

			}
		});

		return mapping;
	}

	public void clearState() {
		childRecordStore.deleteAll();

	}

	public void onRequestSuccess(Object context, Response result) {
		requestHandler.getRequestCallBack().updateProgressMessage(
				((Hashtable) context).get(PROCESS_STATE).toString());
		// sync child with local store
		Child child = (Child) (((Hashtable) context).get(CHILD_TO_SYNC));
		try {
			JSONObject jsonChild = new JSONObject(result.getResult().toString());
			// HttpServer.printResponse(result);
			JSONArray fieldNames = jsonChild.names();
			for (int j = 0; j < fieldNames.length(); j++) {
				String fieldName = fieldNames.get(j).toString();
				String fieldValue = jsonChild.getString(fieldName);
				child.setField(fieldName, fieldValue);
			}
			childPhotoUpdater.updateChildPhoto(child, requestHandler);
			child.syncSuccess();

		} catch (Exception e) {
            e.printStackTrace();
			child.syncFailed(e.getMessage());
		} finally {
			childRecordStore.addOrUpdate(child);
		}

	}

	public void onRequestFailure(Object context, Exception exception) {
		requestHandler.getRequestCallBack().updateProgressMessage(
				((Hashtable) context).get(PROCESS_STATE).toString()
						+ " Failed. ");
		Child child = (Child) (((Hashtable) context).get(CHILD_TO_SYNC));
		child.syncFailed(exception.getMessage());
		childRecordStore.addOrUpdate(child);
	}

}
