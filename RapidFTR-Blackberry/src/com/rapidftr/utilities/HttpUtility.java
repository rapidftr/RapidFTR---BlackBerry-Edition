package com.rapidftr.utilities;

import com.sun.me.web.request.Arg;

public class HttpUtility {

	public static final Arg HEADER_ACCEPT_JSON = new Arg("Accept", "application/json");
	public static final Arg HEADER_CONTENT_TYPE_IMAGE = new Arg("Content-Type", "image/jpeg");
	public static Arg[] makeJSONHeader() {
		return new Arg[]{HEADER_ACCEPT_JSON};
	}
	public static final Arg HEADER_CONTENT_TYPE_AUDIO = new Arg("Content-Type", "audio/amr");

}
