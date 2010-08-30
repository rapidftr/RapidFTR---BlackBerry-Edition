package com.rapidftr.services;

import java.util.Vector;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;

public class ChildStoreService {
	private ChildrenRecordStore childRecordStore;
	public ChildStoreService(ChildrenRecordStore childRecordStore) {
		this.childRecordStore = childRecordStore;
	}

	public Child[] getAllChildrenFromPhoneStoredAsArray() {
		Vector children = childRecordStore.getAllChildren();
		Child[] childList = new Child[children.size()];
		for (int i = 0; i < children.size(); i++) {
			childList[i] = (Child) children.elementAt(i);
		}
		return childList;
	}
	
	public void storeChildrenInPhone(Vector children) {
		childRecordStore.storeChildren(children);
	// TODO add code for syncing data with server.
		// Arg[] httpArgs = new Arg[1];
		// httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		// Response response = httpService.get("children", null, httpArgs);
		// Result result = response.getResult();
		// HttpServer.printResponse(response);
		// try {
		// JSONArray jsonChildren = result.getAsArray("");
		// // Child[] children = new Child[jsonChildren.length()];
		// Child[] children = new Child[10];
		// // for (int i = 0; i < jsonChildren.length(); i++) {
		// for (int i = 0; i < 10; i++) {
		// JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
		// Child child = new Child();
		// JSONArray fieldNames = jsonChild.names();
		// for (int j = 0; j < fieldNames.length(); j++) {
		// String fieldName = fieldNames.get(j).toString();
		// String fieldValue = jsonChild.getString(fieldName);
		// child.setField(fieldName, fieldValue);
		// }
		//
		// children[i] = child;
		// }
		// //return children;
		// } catch (ResultException e) {
		// throw new ServiceException(
		// "JSON returned from get children is in unexpected format");
		// } catch (JSONException e) {
		// throw new ServiceException(
		// "JSON returned from get children is in unexpected format");
		// }
	}

}
