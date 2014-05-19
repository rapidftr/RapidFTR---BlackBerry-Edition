package com.rapidftr.net;

import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.rapidftr.utilities.Constants;

import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.WLANInfo;

public class ConnectionFactory {

    public HttpConnection openConnection(String url) throws IOException {
        if (isNotConnected()) {
            throw new IOException(Constants.CONNECTIONS_ARE_OFFLINE);
        }
        if (isWIFIAvailable()) {
            url = url + ";interface=wifi";
        }
        return (HttpConnection) Connector.open(url, Connector.READ_WRITE, true);
    }

    public boolean isNotConnected() {
        return !(isWIFIAvailable() || isDataServicesAvailable());
    }

    private boolean isDataServicesAvailable() {
        return !CoverageInfo.isOutOfCoverage();
    }

    protected boolean isWIFIAvailable() {
        return WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED;
    }
}

