package com.rapidftr.utilities;

import com.rapidftr.datastore.MockStore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SettingsTest {
    private MockStore fakeStore;

    @Test
    public void shouldGenerateUpdateUrlWithJadFileAttachedToLoginUrl(){
        fakeStore = new MockStore();
        fakeStore.setString(Settings.LAST_USED_HOST_KEY, "https://dev-test.rapidftr.com");
        Settings setting = new Settings(fakeStore);
        String expectedUpdateUrl = "https://dev-test.rapidftr.com/blackberry/RapidFTR.jad";
        assertEquals(expectedUpdateUrl, setting.getApplicationUpdateUrl());

    }
}
