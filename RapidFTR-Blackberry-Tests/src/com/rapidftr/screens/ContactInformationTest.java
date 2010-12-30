package com.rapidftr.screens;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import com.rapidftr.utilities.Store;

public class ContactInformationTest {
	
	private Store store;
	private ContactInformation contact;

	@Before
	public void setUp(){
		store = mock(Store.class);
		contact = new ContactInformation(store);

	}
	
	@Test
	public void getName() throws Exception {
		contact.getName();
		verify(store, times(1)).getString("contact.name");
	}
	
	@Test
	public void getPosition() throws Exception {
		contact.getPosition();
		verify(store, times(1)).getString("contact.position");
	}
	
	@Test
	public void getOrganization() throws Exception {
		contact.getOrganization();
		verify(store, times(1)).getString("contact.organization");
	}
	
	@Test
	public void getEmail() throws Exception {
		contact.getEmail();
		verify(store, times(1)).getString("contact.email");
	}
	
	@Test
	public void getPhone() throws Exception {
		contact.getPhone();
		verify(store, times(1)).getString("contact.phone");
	}
	
	@Test
	public void getLocation() throws Exception {
		contact.getLocation();
		verify(store, times(1)).getString("contact.location");
	}
	
	@Test
	public void getOther() throws Exception {
		contact.getOther();
		verify(store, times(1)).getString("contact.other");
	}
	
	@Test
	public void setName() throws Exception {
		contact.setName("name");
		verify(store, times(1)).setString("contact.name","name");
	}
	
	@Test
	public void setPosition() throws Exception {
		contact.setPosition("position");
		verify(store, times(1)).setString("contact.position","position");
	}
	
	@Test
	public void setOrganization() throws Exception {
		contact.setOrganization("organization");
		verify(store, times(1)).setString("contact.organization","organization");
	}
	
	@Test
	public void setEmail() throws Exception {
		contact.setEmail("email");
		verify(store, times(1)).setString("contact.email","email");
	}
	
	@Test
	public void setPhone() throws Exception {
		contact.setPhone("phone");
		verify(store, times(1)).setString("contact.phone","phone");
	}
	
	@Test
	public void setLocation() throws Exception {
		contact.setLocation("location");
		verify(store, times(1)).setString("contact.location","location");
	}
	
	@Test
	public void setOther() throws Exception {
		contact.setOther("other");
		verify(store, times(1)).setString("contact.other","other");
	}
}
