package com.rapidftr.utilities;

import net.rim.device.api.system.EventLogger;

public class Logger {
    private static final long APP_GUID = 0x4c9d3452d87923f2L;

    private Logger() {}
    
    public static void register() {
        EventLogger.register(APP_GUID, "RapidFTR", EventLogger.VIEWER_STRING);
    }

    public static void log(String message) {
       EventLogger.logEvent( APP_GUID, message.getBytes(), EventLogger.ALWAYS_LOG); 
    }
}
