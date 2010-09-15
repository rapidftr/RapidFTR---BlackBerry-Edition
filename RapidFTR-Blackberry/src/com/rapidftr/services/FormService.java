package com.rapidftr.services;

import com.rapidftr.datastore.FormStore;
import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

public class FormService extends RequestAwareService{

	FormStore formStore;
	public FormService(HttpService httpService,FormStore formStore) {
		super(httpService);
		this.formStore = formStore;
	}

	public void downloadForms() {
		requestHandler.setRequestInProgress();
		Arg[] httpArgs = new Arg[1];
		httpArgs[0] = HttpUtility.HEADER_ACCEPT_JSON;
		httpService.get("published_form_sections", null, httpArgs, requestHandler);
	}


	public String sampleJsonForTesting() {
		return "[{\"name\":\"Basic details\",\"_rev\":\"1-60b4ea56f8ede5871faef8ee0bf593ee\",\"unique_id\":\"basic_details\",\"_id\":\"ad3e0c9d075a13571d53dc6e4a667369\",\"enabled\":\"true\",\"order\":1,\"fields\":[{\"name\":\"name\",\"type\":\"text_field\"},{\"name\":\"age\",\"type\":\"text_field\"},{\"name\":\"age_is\",\"option_strings\":[\"Approximate\",\"Exact\"],\"type\":\"select_box\"},{\"name\":\"gender\",\"option_strings\":[\"Male\",\"Female\"],\"type\":\"radio_button\"},{\"name\":\"origin\",\"type\":\"text_field\"},{\"name\":\"last_known_location\",\"type\":\"text_field\"},{\"name\":\"date_of_separation\",\"option_strings\":[\"\",\"1-2 weeks ago\",\"2-4 weeks ago\",\"1-6 months ago\",\"6 months to 1 year ago\",\"More than 1 year ago\"],\"type\":\"select_box\"},{\"name\":\"current_photo_key\",\"type\":\"photo_upload_box\"}],\"editable\":false,\"couchrest-type\":\"FormSection\",\"description\":\"Basic information about a child\"},{\"name\":\"Family details\",\"_rev\":\"1-d0801e54bfd06915c1624706e86c56f4\",\"unique_id\":\"family_details\",\"_id\":\"236fab881dc8433b5e19671c75efbcb5\",\"enabled\":\"true\",\"order\":2,\"fields\":[{\"name\":\"fathers_name\",\"type\":\"text_field\"},{\"name\":\"reunite_with_father\",\"option_strings\":[\"Yes\",\" No\"],\"type\":\"select_box\"},{\"name\":\"mothers_name\",\"type\":\"text_field\"},{\"name\":\"reunite_with_mother\",\"option_strings\":[\"Yes\",\" No\"],\"type\":\"select_box\"}],\"editable\":true,\"couchrest-type\":\"FormSection\",\"description\":\"Information about a child's known family\"},{\"name\":\"Caregiver details\",\"_rev\":\"7-3309b35c891b71261a0f6c25b3da3bce\",\"unique_id\":\"caregiver_details\",\"_id\":\"b6816954ba4a31e5bcf6c4e351fd1b1d\",\"enabled\":\"true\",\"order\":3,\"fields\":[{\"name\":\"occupation\",\"type\":\"text_field\"},{\"name\":\"caregivers_name\",\"type\":\"text_field\"},{\"name\":\"relationship_to_child\",\"type\":\"text_field\"},{\"name\":\"is_refugee\",\"type\":\"check_box\"},{\"name\":\"trafficked_child\",\"type\":\"check_box\"},{\"name\":\"is_orphan\",\"type\":\"check_box\"},{\"name\":\"in_interim_care\",\"type\":\"check_box\"},{\"name\":\"is_in_child_headed_household\",\"type\":\"check_box\"},{\"name\":\"sick_or_injured\",\"type\":\"check_box\"},{\"name\":\"possible_physical_or_sexual_abuse\",\"type\":\"check_box\"},{\"name\":\"is_disabled\",\"type\":\"check_box\"},{\"name\":\"tom\",\"help_text\":\"Tom's new field\",\"enabled\":\"1\",\"type\":\"text_field\"}],\"editable\":true,\"couchrest-type\":\"FormSection\",\"description\":\"Information about the child's current caregiver\"},{\"name\":\"School Details\",\"_rev\":\"4-bd5563d2559421574a81a3ee210e189b\",\"unique_id\":\"school_details\",\"_id\":\"8e3c3d084029fa768151a218eb7ba3b9\",\"enabled\":true,\"order\":4,\"fields\":[{\"name\":\"School Name\",\"help_text\":\"\",\"enabled\":\"1\",\"type\":\"text_field\"},{\"name\":\"Tom's Last Name?\",\"help_text\":\"\",\"enabled\":\"1\",\"type\":\"text_field\"},{\"name\":\"Tom's Middle Name?\",\"help_text\":\"\",\"enabled\":\"1\",\"type\":\"text_field\"}],\"editable\":true,\"couchrest-type\":\"FormSection\",\"description\":\"\"},{\"name\":\"Tom form\",\"_rev\":\"1-dfc735af3e855214455eb7fde7ef3e6d\",\"unique_id\":\"tom_form\",\"_id\":\"eaa2ce0408c964adf57bc9c9305f1836\",\"enabled\":true,\"order\":5,\"fields\":[],\"editable\":true,\"couchrest-type\":\"FormSection\",\"description\":\"\"}]";
	}


	public void onRequestSuccess(Object context, Response result) {
		try {
			formStore.storeForms(result.getResult().toString());
		} catch (Exception e) {

			requestHandler.markProcessFailed();
			return;
		}
		requestHandler.markProcessComplete();		
	}

	public void clearState() {
	formStore.clearState();
	}

}
