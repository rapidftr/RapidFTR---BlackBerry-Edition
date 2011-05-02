package com.rapidftr.services;

import com.rapidftr.datastore.MockStore;
import com.rapidftr.net.HttpService;
import com.rapidftr.screens.ContactInformation;
import com.sun.me.web.path.Result;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Response;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContactInformationSyncServiceTest {


    private HttpService httpService;
    private ContactInformation contactInformation;
    private ContactInformationSyncService service;

    @Before
    public void setUp() throws Exception {
        httpService = mock(HttpService.class);
        contactInformation = new ContactInformation(new MockStore());
        service = new ContactInformationSyncService(httpService, contactInformation);
    }

    @Test
    public void shouldUpdateContactInformationsFromResponse() throws ResultException {
        Response response = mock(Response.class);
        String name = "someone";
        String position = "manager";
        String organization = "UNICEF";
        String email = "someone@someone.com";
        String phone = "122112211";
        String location = "Sydney";
        String otherInformation = "some other information";

        String responseContent = String.format("{ " +
                "name: %s," +
                "position: %s," +
                "organization: %s," +
                "email: %s," +
                "phone: %s," +
                "location: %s," +
                "other_information: %s}", name, position, organization, email, phone, location, otherInformation);

        Result result = Result.fromContent(responseContent, Result.JSON_CONTENT_TYPE);
        when(response.getResult()).thenReturn(result);

        service.onRequestSuccess(null, response);

        assertEquals(contactInformation.getName(), name);
        assertEquals(contactInformation.getPosition(), position);
        assertEquals(contactInformation.getOrganization(), organization);
        assertEquals(contactInformation.getEmail(), email);
        assertEquals(contactInformation.getPhone(), phone);
        assertEquals(contactInformation.getLocation(), location);
        assertEquals(contactInformation.getOther(), otherInformation);
    }
}
