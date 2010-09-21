package com.rapidftr.net;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.WLANInfo;

public class HttpConnectionFactory {

	public static HttpConnection openOldConnection(String url) throws IOException
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
	
	public static HttpConnection openConnection(String url) throws IOException {
		
		boolean wifiCoverage = WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED;
		if(wifiCoverage) {
			return  (HttpConnection) Connector.open(url+";interface=wifi",Connector.READ_WRITE,true);
		} else if (!CoverageInfo.isOutOfCoverage()){
			return (HttpConnection) Connector.open(url,Connector.READ_WRITE,true);	
		} else {
			throw new IOException("Could not establish connection with host because all connectors are offline");
		}
			
	}
}
