package com.rapidftr.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AudioFieldTest {
	@Test
	public void shouldReturnAudioFieldIfTheTypeIsSame(){
		assertNotNull(AudioField.createdFormField("someName", AudioField.TYPE, ""));
		assertNull(AudioField.createdFormField("someName", "someOtherType", ""));
	}
	
	@Test
	public void shouldRecordAudio(){
		//This cannot be tested by mocking as the Player is created through a static factory method.
	}

}
