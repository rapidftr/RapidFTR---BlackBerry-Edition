
package com.rapidftr.net;

import com.sun.me.web.request.Response;

public interface ControllerCallback {
	void onRequestFailure(Exception exception);
	void onRequestSuccess(Object context, Response result);
}
