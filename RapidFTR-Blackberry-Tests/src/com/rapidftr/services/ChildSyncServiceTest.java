package com.rapidftr.services;

import java.util.Hashtable;

import javax.microedition.io.HttpConnection;

import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.net.HttpBatchRequestHandler;
import com.rapidftr.net.HttpServer;
import com.rapidftr.net.HttpService;
import com.rapidftr.process.ChildSyncProcess;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Part;
import com.sun.me.web.request.PostData;
import com.sun.me.web.request.Response;


import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class ChildSyncServiceTest {
	@Mock
	HttpService httpService;
	@Mock
	ChildrenRecordStore store;
	@Mock
	Child child;
	@Mock
	Hashtable context;
	
	@Mock
	HttpBatchRequestHandler requestHandler ;
	
	ChildSyncService childService ;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		childService = new ChildSyncService(httpService, store);
	}

	@Test
	public void testSyncChildRecord() {
		Hashtable requestContext = new Hashtable();
		
		Part array[] = new Part[0];
		when(child.isNewChild()).thenReturn(true);
		when(child.getPostData()).thenReturn(new PostData(array, "abced"));
		PostData postData = child.getPostData();
		
		childService.requestHandler = requestHandler;
		
		Arg multiPart = new Arg("Content-Type",
				"multipart/form-data;boundary=" + postData.getBoundary());
		
		Arg json = HttpUtility.HEADER_ACCEPT_JSON;
		
		Arg[] httpArgs = { multiPart, json };
		
		requestContext.put(childService.PROCESS_STATE, "Uploading [" + 1 + "/"+ 1 + "]");
		requestContext.put(childService.CHILD_TO_SYNC, child);
		
		childService.syncChildRecord(child);
		
		
		verify(requestHandler).post("children", null,httpArgs, postData,requestContext);
	}

	@Test
	public void testSyncAllChildRecords() {
		//fail("Not yet implemented");
	}

	@Test
	public void testOnRequestSuccess() throws ResultException {
		Response response = stubSuccessfulResponse();
		when(context.get(childService.CHILD_TO_SYNC)).thenReturn(child);
		when(context.get(childService.PROCESS_STATE)).thenReturn("Some State");
		childService.onRequestSuccess((Object)context, response);
		verify(store).addOrUpdateChild(child);
	}

	@Test
	public void testOnRequestFailure() {
		Exception exeption=mock(Exception.class);
		when(exeption.getMessage()).thenReturn("exception");
		when(context.get(childService.CHILD_TO_SYNC)).thenReturn(child);
		when(context.get(childService.PROCESS_STATE)).thenReturn("Some State");
		childService.onRequestFailure((Object)context,exeption);
		verify(store).addOrUpdateChild(child);
		verify(child).syncFailed(exeption.getMessage());
	}
	
	//	@Ignore
//	public void should_fetch_from_server_and_parse_child_objects_from_response()
//			throws Exception {
//		HttpService httpService = mock(HttpService.class);
//		ChildSyncService childService = new ChildSyncService(httpService, null);
//
//		Response successfulResponse = stubSuccessfulResponse();
//		// when(childService.get("/children")).thenReturn(successfulResponse);
//
//		Child[] children = childService.getAllChildren();
//
//		assertThat(children.length, is(1));
//		assertThat(children[0].getName(), is("Dave"));
//		assertThat(children[0].getField("age_is"), is("approximate"));
//		assertThat(children[0].getField("mothers_name"), is("Doris"));
//	}
//
	private Response stubSuccessfulResponse() throws ResultException {
		Response response = mock(Response.class);
		String jsonChlidrenString = String
				.format("{\"is_in_child_headed_household\":\"No\",\"relationship_to_child\":\"\",\"occupation\":\"\",\"date_of_separation\":\"\",\"name\":\"Dave\",\"created_at\":\"15/05/2010 15:15\",\"in_interim_care\":\"No\",\"trafficked_child\":\"No\",\"age_is\":\"approximate\",\"_rev\":\"1-ab2ba5ebda94f87dbf9c11ca99c9e280\",\"reunite_with_mother\":\"Yes\",\"unique_identifier\":\"rapidftrsco0a3cd\",\"_id\":\"0237970b10c0bcdbcdad9f306e7b426f\",\"mothers_name\":\"Doris\",\"reunite_with_father\":\"Yes\",\"created_by\":\"rapidftr\",\"possible_physical_or_sexual_abuse\":\"No\",\"last_known_location\":\"Scotland\",\"origin\":\"\",\"couchrest-type\":\"Child\",\"is_refugee\":\"No\",\"is_disabled\":\"No\",\"sick_or_injured\":\"No\",\"histories\":[],\"is_orphan\":\"No\",\"caregivers_name\":\"\",\"age\":\"12\",\"fathers_name\":\"\"}");
		when(response.getResult()).thenReturn(
				Result.fromContent(jsonChlidrenString, "application/json"));
		when(response.getCode()).thenReturn(200);
		return response;
	}
}
