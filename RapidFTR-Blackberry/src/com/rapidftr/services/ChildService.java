package com.rapidftr.services;

import com.rapidftr.model.Child;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import java.io.IOException;

public class ChildService {

	private final HttpService httpService;

	public ChildService(HttpService httpService) {
		this.httpService = httpService;
	}

	public Child[] getAllChildren() throws IOException {

		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		Response response = httpService.get("children", null,
				httpArgs);
		Result result = response.getResult();
		HttpServer.printResponse(response);
		try {
			JSONArray jsonChildren = result.getAsArray("");
			Child[] children = new Child[jsonChildren.length()];
			for (int i = 0; i < jsonChildren.length(); i++) {
				JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
				Child child = new Child();
				JSONArray fieldNames = jsonChild.names();
				for (int j = 0; j < fieldNames.length(); j++) {
					String fieldName = fieldNames.get(j).toString();
					String fieldValue = jsonChild.getString(fieldName);
					child.setField(fieldName, fieldValue);
				}

				children[i] = child;
			}
			return children;
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from get children is in unexpected format");
		} catch (JSONException e) {
			throw new ServiceException(
					"JSON returned from get children is in unexpected format");
		}
	}
}
