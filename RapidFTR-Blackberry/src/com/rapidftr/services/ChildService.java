package com.rapidftr.services;

import java.io.IOException;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.rapidftr.model.Child;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Response;

public class ChildService {

    private final HttpService httpService;

    public ChildService(HttpService httpServer) {
        this.httpService = httpServer;
    }

    public Child[] getAllChildren() throws IOException {
//        Response response = httpService.getFromServer("/children");
//        Result result = response.getResult();
//        try {
//            JSONArray jsonChildren = result.getAsArray("");
//            Child[] children = new Child[jsonChildren.length()];
//            for (int i = 0; i < jsonChildren.length(); i++) {
//                JSONObject jsonChild = (JSONObject) jsonChildren.get(i);
//                Child child = new Child(jsonChild.getString("name"));
//                JSONArray fieldNames = jsonChild.names();
//                for (int j = 0; j < fieldNames.length(); j++) {
//                    String fieldName = fieldNames.get(j).toString();
//                    String fieldValue = jsonChild.getString(fieldName);
//                    child.setField(fieldName, fieldValue);
//                }
//
//                children[i] = child;
//            }
//            return children;     
//        } catch (ResultException e) {
//            throw new ServiceException("JSON returned from get children is in unexpected format");
//        } catch (JSONException e) {
//            throw new ServiceException("JSON returned from get children is in unexpected format");
//        }
    	return null;
    }
}
