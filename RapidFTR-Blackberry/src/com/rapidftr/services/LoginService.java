package com.rapidftr.services;

import com.rapidftr.net.HttpService;
import com.rapidftr.utilities.HttpUtility;
import com.sun.me.web.path.ResultException;
import com.sun.me.web.request.Arg;
import com.sun.me.web.request.Response;

import net.rim.blackberry.api.phone.Phone;
import net.rim.device.api.system.GPRSInfo;

import java.util.Hashtable;

public class LoginService extends RequestAwareService {
    private LoginSettings settings;
    private static final String USER_NAME = "user_name";
    private static final String PASSWORD = "password";
    private static final String IMEI = "imei";
    private static final String MOBILE_NUMBER = "mobile_number";

    public LoginService(HttpService httpService, LoginSettings settings) {
        super(httpService);
        this.settings = settings;
    }

    public void login(String user, String password) {
        String imei = GPRSInfo.imeiToString(GPRSInfo.getIMEI());
        String mobile = Phone.getDevicePhoneNumber(false);
        
        Arg[] postArgs = new Arg[]{new Arg(USER_NAME, user),
                new Arg(PASSWORD, password),
                new Arg(IMEI, imei),
                new Arg(MOBILE_NUMBER, mobile)};

        Hashtable context = new Hashtable();
        context.put(USER_NAME, user);
        context.put(PASSWORD, password);

        requestHandler.startNewProcess();
        requestHandler.post("sessions", postArgs, HttpUtility.makeJSONHeader(),
                null, context);

    }

    private String parseAuthorizationToken(Response response) {
        try {
            return response.getResult().getAsString("session.token");
        } catch (ResultException e) {
            throw new ServiceException(
                    "JSON returned from login service in unexpected format");
        }
    }

    public void clearLoginState() {
        settings.clear();
    }

    public void onRequestSuccess(Object context, Response result) {
        String user = getUserFromContext((Hashtable) context);
        settings.authenticate(user, parseAuthorizationToken(result));
        settings.setLastUsedUserName(user);
        settings.setLastUsedPassword(getPasswordFromContext((Hashtable) context));
    }

    public void onRequestFailure(Object context, Exception exception) {

        requestHandler.markProcessFailed(" Login Failed Due to "
                + exception.getMessage() + ". ");
    }

    private String getUserFromContext(Hashtable table) {
        return (String) table.get(USER_NAME);
    }

    private String getPasswordFromContext(Hashtable table) {
        return (String) table.get(PASSWORD);
    }

    public boolean offlineLogin(String user, String password) {
        if (settings.allowOfflineLogin(user, password)) {
            settings.authenticate(user, settings.getAuthorizationTokenForOfflineLogin());
            settings.setAuthorisationTokenForOfflineLogin("");
            return true;
        }
        return false;
    }
}
