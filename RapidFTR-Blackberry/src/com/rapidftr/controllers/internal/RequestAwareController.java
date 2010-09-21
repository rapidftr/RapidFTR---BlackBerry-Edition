package com.rapidftr.controllers.internal;

import com.rapidftr.net.ControllerCallback;
import com.rapidftr.net.HttpRequestHandler;
import com.rapidftr.net.ScreenCallBack;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.RequestAwareService;
import com.rapidftr.services.RequestCallBackImpl;

public class RequestAwareController extends Controller implements ControllerCallback {
	protected HttpRequestHandler requestHandler;
	protected RequestAwareService service;
	private ScreenCallBack screenCallBack;
	public RequestAwareController(CustomScreen screen, UiStack uiStack ,RequestAwareService service) {
		super(screen, uiStack);
		this.service = service;
		requestHandler = service.getRequestHandler();
		if(requestHandler!=null){
		((RequestCallBackImpl)requestHandler.getRequestCallBack()).setScreenCallback((ScreenCallBack)screen);
		((RequestCallBackImpl)requestHandler.getRequestCallBack()).setControllerCallback(this);
		}
		screen.setController(this);	
		screenCallBack = (ScreenCallBack) screen;
	}
	
	public void onProcessComplete() {
		//screenCallBack.onProcessComplete();
	}

	public ScreenCallBack getScreenCallBack() {
		return screenCallBack;
	}

}
