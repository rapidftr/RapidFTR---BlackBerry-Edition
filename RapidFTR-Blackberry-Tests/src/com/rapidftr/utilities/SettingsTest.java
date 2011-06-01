package com.rapidftr.utilities;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.rapidftr.Key;
import com.rapidftr.datastore.MockStore;

public class SettingsTest {
    private MockStore fakeStore;

    @Test
    public void shouldGenerateUpdateUrlWithJadFileAttachedToLoginUrl(){
        fakeStore = new MockStore(new Key("fake"));
        fakeStore.setString(Settings.LAST_USED_HOST_KEY, "https://dev-test.rapidftr.com");
        Settings setting = new Settings(fakeStore);
        String expectedUpdateUrl = "https://dev-test.rapidftr.com/blackberry/RapidFTR.jad";
        assertEquals(expectedUpdateUrl, setting.getApplicationUpdateUrl());

    }
}
