package com.rapidftr.services;

import com.rapidftr.model.Child;
import com.rapidftr.utilities.HttpServer;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Response;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class ChildServiceImpl implements ChildService {

    private final HttpServer httpServer;

    public ChildServiceImpl(HttpServer httpServer) {
        this.httpServer = httpServer;
    }

    public Child[] getAllChildren() {
        Response response = httpServer.getFromServer("/children");
        Result result = response.getResult();
        try {
            JSONArray jsonChildren = result.getAsArray("");
            Child[] children = new Child[jsonChildren.length()];
            for (int i = 0; i < jsonChildren.length(); i++) {
                JSONObject jsonChild = (JSONObject) jsonChildren.get(0);
                children[i] = new Child(jsonChild.getString("name"));
            }
            return children;     
        } catch (ResultException e) {
            throw new ServiceException("JSON returned from get children is in unexpected format");
        } catch (JSONException e) {
            throw new ServiceException("JSON returned from get children is in unexpected format");
        }
    }
}
