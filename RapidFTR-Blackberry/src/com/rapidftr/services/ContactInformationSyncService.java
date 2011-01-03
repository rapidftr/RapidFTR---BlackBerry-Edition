package com.rapidftr.services;

import org.json.me.JSONObject;

import com.rapidftr.net.HttpService;
import com.rapidftr.screens.ContactInformation;
import com.rapidftr.screens.ContactInformationScreen;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Response;

public class ContactInformationSyncService extends RequestAwareService {
	
	private final ContactInformation contact;

	public ContactInformationSyncService(HttpService httpService, ContactInformation contactInformation) {
		super(httpService);
		this.contact = contactInformation;
	}


	public void downloadContactInformation() {
        requestHandler.startNewProcess();
        requestHandler.get("contact_information/administrator", null, HttpUtility.makeJSONHeader(), null);
    }


    public void onRequestSuccess(Object context, Response response) {
        try {
        	JSONObject jsonObject = new JSONObject(response.getResult().toString());
        	contact.setName(jsonObject.getString("name"));
        	contact.setPosition(jsonObject.getString("position"));
        	contact.setOrganization(jsonObject.getString("organization"));
        	contact.setEmail(jsonObject.getString("email"));
        	contact.setPhone(jsonObject.getString("phone"));
        	contact.setLocation(jsonObject.getString("location"));
        	contact.setOther(jsonObject.getString("other_information"));
            
        } catch (Exception e) {
        }
    }

    public void onRequestFailure(Object context, Exception exception) {
	}


	public void setScreenCallback(ContactInformationScreen screen) {
		((RequestCallBackImpl)getRequestHandler().getRequestCallBack()).setScreenCallback(screen);
		
	}
}
