package com.rapidftr.form;

import static org.junit.Assert.*;

import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;


public class JSONDataTest {

	@Test
	public void getPropertyWithValidKey() throws Exception {
		JSONObject mockJsonObject = mock(JSONObject.class);
		when(mockJsonObject.getString("testKey")).thenReturn("value");
		JSONData jsonData = new JSONData(mockJsonObject);
		assertEquals("value", jsonData.getProperty("testKey"));
	}
	
	@Test
	public void getPropertyWithInvalidKey() throws Exception {
		JSONObject mockJsonObject = mock(JSONObject.class);
		when(mockJsonObject.getString("testKey")).thenThrow(new JSONException(""));
		JSONData jsonData = new JSONData(mockJsonObject);
		assertEquals("", jsonData.getProperty("testKey"));
	}
}
