package com.rapidftr.services;

import java.util.Vector;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.rapidftr.datastore.ChildrenRecordStore;
import com.rapidftr.model.Child;
import com.rapidftr.model.SearchChildFilter;

//package com.rapidftr.services;
//
//import com.rapidftr.model.Child;
//import com.rapidftr.net.HttpServer;
//import com.rapidftr.net.HttpService;
//import com.sun.me.web.path.Result;
//import com.sun.me.web.path.ResultException;
//import com.sun.me.web.request.Response;
//
//import org.junit.Ignore;
//import org.junit.Test;
//
//import static org.hamcrest.core.Is.is;
//import static org.junit.Assert.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//public class ChildServiceTest {
//
//    @Test
//    @Ignore
//    public void should_fetch_from_server_and_parse_child_objects_from_response() throws Exception {
//        HttpService httpService = mock(HttpService.class);
//        ChildService childService = new ChildService(httpService);
//        
//        
//
//        Response successfulResponse = stubSuccessfulResponse();
//      //  when(childService.get("/children")).thenReturn(successfulResponse);
//
//        Child[] children = childService.getAllChildren();
//
//        assertThat(children.length, is(1));
//        assertThat(children[0].getName(), is("Dave"));
//        assertThat(children[0].getField("age_is"), is("approximate"));
//        assertThat(children[0].getField("mothers_name"), is("Doris"));
//    }
//
//
//     private Response stubSuccessfulResponse() throws ResultException {
//        Response response = mock(Response.class);
//        String jsonChlidrenString = String.format("[{\"is_in_child_headed_household\":\"No\",\"relationship_to_child\":\"\",\"occupation\":\"\",\"date_of_separation\":\"\",\"name\":\"Dave\",\"created_at\":\"15/05/2010 15:15\",\"in_interim_care\":\"No\",\"trafficked_child\":\"No\",\"age_is\":\"approximate\",\"_rev\":\"1-ab2ba5ebda94f87dbf9c11ca99c9e280\",\"reunite_with_mother\":\"Yes\",\"unique_identifier\":\"rapidftrsco0a3cd\",\"_id\":\"0237970b10c0bcdbcdad9f306e7b426f\",\"mothers_name\":\"Doris\",\"reunite_with_father\":\"Yes\",\"created_by\":\"rapidftr\",\"possible_physical_or_sexual_abuse\":\"No\",\"last_known_location\":\"Scotland\",\"origin\":\"\",\"couchrest-type\":\"Child\",\"is_refugee\":\"No\",\"is_disabled\":\"No\",\"sick_or_injured\":\"No\",\"histories\":[],\"is_orphan\":\"No\",\"caregivers_name\":\"\",\"age\":\"12\",\"fathers_name\":\"\"}]");
//        when(response.getResult()).thenReturn(Result.fromContent(jsonChlidrenString, "application/json"));
//        when(response.getCode()).thenReturn(200);
//        return response;
//    }
//}
public class ChildStoreServiceTest{
	
	private  ChildrenRecordStore childRecordStore;
	@Before
	public void setUp() {
		childRecordStore = mock(ChildrenRecordStore.class);
	}
	
	@Test
	public void ifPersistentStoreEmptySearchShouldResultEmptyArray()
	{	
		when(childRecordStore.getAllChildren()).thenReturn(new Vector());
		ChildStoreService childStoreService = new ChildStoreService(childRecordStore );
		Assert.assertEquals(childStoreService.searchChildrenFromStore(new SearchChildFilter("Tom")).length,0);
	}
	
	@Test
	public void ifChildIsNotPresentInPersistentStoreSearchShouldReturnEmptyResults()
	{
		Vector persistentStore=new Vector();
		Child child = new Child();
		child.setField("name", "Tom");
		persistentStore.add(child);
		when(childRecordStore.getAllChildren()).thenReturn(persistentStore);
		ChildStoreService childStoreService = new ChildStoreService(childRecordStore );
		Assert.assertEquals(childStoreService.searchChildrenFromStore(new SearchChildFilter("Harry")).length,0);
	}
	
	@Test
	public void ifChildWithNamePresentInPersistentStoreSearchShouldReturnTheChild()
	{
		Vector persistentStore=new Vector();
		
		Child child = new Child();
		child.setField("name", "Tom");
		persistentStore.add(child);
		
		when(childRecordStore.getAllChildren()).thenReturn(persistentStore);
		
		ChildStoreService childStoreService = new ChildStoreService(childRecordStore );
		Child[] searchResults = childStoreService.searchChildrenFromStore(new SearchChildFilter("Tom"));
		
		Assert.assertEquals(searchResults.length,1);
		Assert.assertEquals(searchResults[0],child);
	}
	
	@Test
	public void searchShouldReturnAllTheChildrenWithSearchedName()
	{
		Vector persistentStore=new Vector();
		
		Child child = new Child();
		child.setField("name", "Tom");
		persistentStore.add(child);
		
		Child child2 = new Child();
		child2.setField("name", "Tom");
		persistentStore.add(child2);
		
		when(childRecordStore.getAllChildren()).thenReturn(persistentStore);
		
		ChildStoreService childStoreService = new ChildStoreService(childRecordStore );
		Child[] searchResults = childStoreService.searchChildrenFromStore(new SearchChildFilter("Tom"));
		
		Assert.assertEquals(searchResults.length,2);
		Assert.assertEquals(searchResults[0],child);
		Assert.assertEquals(searchResults[1],child2);
	}
	
	@Test
	public void ifChildWithIdPresentInPersistentStoreSearchShouldReturnTheChild()
	{
		Vector persistentStore=new Vector();
		
		Child child = new Child();
		child.setField("name", "Tom");
		child.setField("unique_identifier", "1");
		persistentStore.add(child);
		
		when(childRecordStore.getAllChildren()).thenReturn(persistentStore);
		
		ChildStoreService childStoreService = new ChildStoreService(childRecordStore );
		Child[] searchResults = childStoreService.searchChildrenFromStore(new SearchChildFilter("1"));
		
		Assert.assertEquals(searchResults.length,1);
		Assert.assertEquals(searchResults[0],child);
	}
	
	@Test
	public void ifNameofOneChildAndIdOfOtherAreEqualSearchShouldReturnBothTheChildren()
	{
	Vector persistentStore=new Vector();
		
		Child child = new Child();
		child.setField("name", "1");
		persistentStore.add(child);
		
		Child child2 = new Child();
		child2.setField("unique_identifier", "1");
		persistentStore.add(child2);
		
		when(childRecordStore.getAllChildren()).thenReturn(persistentStore);
		
		ChildStoreService childStoreService = new ChildStoreService(childRecordStore );
		Child[] searchResults = childStoreService.searchChildrenFromStore(new SearchChildFilter("1"));
		
		Assert.assertEquals(searchResults.length,2);
		Assert.assertEquals(searchResults[0],child);
		Assert.assertEquals(searchResults[1],child2);
	}
}