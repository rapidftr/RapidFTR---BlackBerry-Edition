package com.rapidftr.services;

import com.rapidftr.datastore.ChildAction;
import com.rapidftr.datastore.Children;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Response;
import org.json.me.JSONArray;
import org.json.me.JSONObject;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ChildSyncService extends RequestAwareService {

	public static final String PROCESS_STATE = "process_state";

	public static final String CHILD_TO_SYNC = "childToSync";

	private final ChildrenRecordStore childRecordStore;
    private ChildPhotoUpdater childPhotoUpdater;
    private HttpService httpService;

    public ChildSyncService(HttpService httpService,
			ChildrenRecordStore childRecordStore) {
        this(httpService, childRecordStore, new ChildPhotoUpdater(httpService, childRecordStore));
    }

	public ChildSyncService(HttpService httpService,
			ChildrenRecordStore childRecordStore, ChildPhotoUpdater photoUpdater) {
		// TODO get rid of dependency on RequestAwareService/requesthandler
        super(httpService);
        this.childRecordStore = childRecordStore;
        this.childPhotoUpdater = photoUpdater;
        this.httpService = httpService;
	}

	private void uploadChildren(final Children children, final ChildSyncListener listener) {

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
					httpService.post("children", null, httpArgs, listener, postData,
							context);
				} else if (child.isUpdated()) {
					httpService.put("children/" + child.getField("_id"),
							null, httpArgs, listener, postData, context);
				}

			}
		});

	}

	public void syncChildRecord(Child child) {
		Vector childrenList = new Vector();
		childrenList.addElement(child);
        ChildSyncListener listener =
                new ChildSyncListener(requestHandler.getRequestCallBack(), 1, childRecordStore, childPhotoUpdater);
		uploadChildren(new Children(childrenList), listener);
	}

	public void syncAllChildRecords() throws ServiceException {
		new Thread() {
			public void run() {
				requestHandler.getRequestCallBack().updateProgressMessage(
						"Syncing");
                Children childrenToUpload = childrenToBeUploaded();
                try {
                    final Vector childrenToBeDownloaded = childRecordsNeedToBeDownload();
                    ChildSyncListener listener =
                                    new ChildSyncListener(requestHandler.getRequestCallBack(),
                                            childrenToUpload.count() + childrenToBeDownloaded.size(),
                                            childRecordStore, childPhotoUpdater);
                    uploadChildren(childrenToUpload, listener);
                    downloadNewChildRecords(childrenToBeDownloaded, listener);
                } catch (Exception e) {
                    e.printStackTrace();
                    requestHandler.markProcessFailed("Error syncing children");
                }
			};
		}.start();

	}

    private Children childrenToBeUploaded() {
        return childRecordStore.getAll().getChildrenToUpload();
    }

    private void downloadNewChildRecords(Vector childrenToBeDownloaded, final ChildSyncListener listener) {
		Enumeration items = childrenToBeDownloaded.elements();
		int index = 0;
		while (items.hasMoreElements()) {
			index++;
			Hashtable context = new Hashtable();
			context.put(PROCESS_STATE, "Downloading [" + index + "/"
					+ childrenToBeDownloaded.size() + "]");
			Child child = new Child();
			String childId = items.nextElement().toString();
			child.setField("_id", childId);
			child.setField("name", childId);
			child.setField("last_known_location", "NA");
			context.put(CHILD_TO_SYNC, child);
			httpService.get("children/" + childId, null, HttpUtility
					.makeJSONHeader(), listener, context);
		}

	}

	private Vector childRecordsNeedToBeDownload() throws Exception {
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

	private Hashtable getOnlineStoredChildrenIdRevMapping() throws Exception {

		Hashtable mapping = new Hashtable();

		Response response;
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

		return mapping;
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

    // TODO this is not used anymore - to remove this need to get rid of dependency on RequestAwareService
    public void onRequestFailure(Object context, Exception exception) {
    }

    public void onRequestSuccess(Object context, Response response) {
    }
}
