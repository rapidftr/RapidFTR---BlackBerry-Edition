package com.rapidftr.services;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Response;

public class FormService extends RequestAwareService {

    FormStore formStore;

    public FormService(HttpService httpService, FormStore formStore) {
        super(httpService);
        this.formStore = formStore;
    }

    public void downloadForms() {
        requestHandler.startNewProcess();
        requestHandler.get("published_form_sections", null, HttpUtility.makeJSONHeader(), null);
    }


    public void onRequestSuccess(Object context, Response result) {
        try {
            formStore.storeForms(result.getResult().toString());
        } catch (Exception e) {
            sendFailedMsg();
            return;
        }
        requestHandler.markProcessComplete(null,null);
    }

    private void sendFailedMsg() {
        requestHandler.markProcessFailed("Sync Failed.Check your network status.");
    }

    public void clearState() {
        formStore.clearState();
    }

    public void onRequestFailure(Object context, Exception exception) {
    }

}
