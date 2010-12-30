package com.rapidftr.services;

import com.rapidftr.net.HttpService;
import com.rapidftr.screens.ContactInformation;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Response;

public class ContactInformationSyncService extends RequestAwareService {
	
	private final ContactInformation contact;

	public ContactInformationSyncService(HttpService httpService, ContactInformation contactInformation) {
		super(httpService);
		// TODO Auto-generated constructor stub
		this.contact = contactInformation;
	}


	public void downloadForms() {
        requestHandler.startNewProcess();
        requestHandler.get("contact_information/administrator", null, HttpUtility.makeJSONHeader(), null);
    }


    public void onRequestSuccess(Object context, Response response) {
        try {
        	contact.setName(response.getResultString("name"));
        	contact.setPosition(response.getResultString("position"));
        	contact.setOrganization(response.getResultString("organization"));
        	contact.setEmail(response.getResultString("email"));
        	contact.setPhone(response.getResultString("phone"));
        	contact.setLocation(response.getResultString("location"));
        	contact.setOther(response.getResultString("other_information"));
            
        } catch (Exception e) {
            sendFailedMsg();
            return;
        }
        requestHandler.markProcessComplete();
    }

    private void sendFailedMsg() {
        requestHandler.markProcessFailed("Unable to fetch contact information. Check your network status.");
    }
    
    public void onRequestFailure(Object context, Exception exception) {
		// TODO Auto-generated method stub
	}
}
