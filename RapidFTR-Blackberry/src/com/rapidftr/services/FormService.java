package com.rapidftr.services;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.HttpConnection;

import net.rim.device.api.io.http.HttpServerConnection;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.rapidftr.controllers.SynchronizeFormsController;
import com.rapidftr.model.Child;
import com.rapidftr.model.Form;
import com.rapidftr.model.FormField;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Request;
import com.sun.me.web.request.RequestListener;
import com.sun.me.web.request.Response;

public class FormService implements RequestListener {

	private HttpService httpService;
	private FormServiceListener formServiceListener;

	public FormService(HttpService httpService) {
		this.httpService = httpService;
	}

	private Vector parseFormsFormJSONResponse(Response response) {

		Result result = response.getResult();
		try {
			JSONArray jsonForms = result.getAsArray("");
			Vector forms = new Vector();
			for (int i = 0; i < jsonForms.length(); i++) {
				JSONObject jsonForm = (JSONObject) jsonForms.get(i);
				Vector formFields = new Vector();
				JSONArray jsonFormFields = jsonForm.getJSONArray("fields");
				for (int j = 0; j < jsonFormFields.length(); j++) {
					JSONObject jsonFormField = jsonFormFields.getJSONObject(j);
					Vector optionStrings = new Vector();
					try {
						JSONArray jsonOptionString = jsonFormField
								.getJSONArray("option_strings");
						for (int k = 0; k < jsonOptionString.length(); k++) {
							optionStrings.addElement(jsonOptionString
									.getString(k));
						}
					} catch (JSONException e) {

					}

					formFields.addElement(new FormField(jsonFormField
							.getString("name"),
							jsonFormField.getString("type"), optionStrings));
				}
				Form form = new Form(jsonForm.getString("name"), jsonForm
						.getString("unique_id"), formFields);
				forms.addElement(form);
			}
			return forms;
		} catch (ResultException e) {
			throw new ServiceException(
					"JSON returned from get forms is in unexpected format");
		} catch (JSONException e) {
			throw new ServiceException(
					"JSON returned from get forms is in unexpected format");
		}
	}

	public void downloadForms() {

		httpService.get("published_form_sections", null, this);
	}

	public void done(Object context, Response result) throws Exception {

		//HttpServer.printResponse(result);

		if (result.getCode() == HttpConnection.HTTP_UNAUTHORIZED) {
			formServiceListener.onAuthenticationFailure();
			return;
		}

		if (result.getCode() != HttpConnection.HTTP_OK) {
			formServiceListener.onConnectionProblem();
			return;
		}

		// synchronizeFormsController.downloadCompleted(null);

		try
		{
		Vector forms = parseFormsFormJSONResponse(result);
		formServiceListener.onDownloadComplete(forms);
		}
		
		catch(Exception e)
		{
			formServiceListener.downloadFailed();
		}
	}

	public void readProgress(Object context, int bytes, int total) {

		formServiceListener.updateDownloadStatus(bytes, total);

	}

	public void writeProgress(Object context, int bytes, int total) {

	}

	public void cancelDownloadOfForms() {

	}

	public void setListener(FormServiceListener formServiceListener) {
		this.formServiceListener = formServiceListener;

	}

}
