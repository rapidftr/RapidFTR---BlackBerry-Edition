package com.rapidftr.net;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.RadioInfo;

public class HttpConnectionFactory {

	public static HttpConnection openConnection(String url) throws IOException
	{
		if ( ( RadioInfo.getActiveWAFs() & RadioInfo.WAF_WLAN ) != 0 )
        {
            if(CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_DIRECT, RadioInfo.WAF_WLAN, true))
               {
                 //Wifi Connected
            	 return  (HttpConnection) Connector.open(url+";interface=wifi",Connector.READ_WRITE,true);
               }
        }
		
			return (HttpConnection) Connector.open(url,Connector.READ_WRITE,true);	
	}	
}
