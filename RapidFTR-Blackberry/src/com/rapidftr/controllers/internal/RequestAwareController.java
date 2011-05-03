package com.rapidftr.controllers.internal;

import com.rapidftr.net.HttpBatchRequestHandler;
import com.rapidftr.screens.internal.CustomScreen;
import com.rapidftr.screens.internal.UiStack;
import com.rapidftr.services.ControllerCallback;
import com.rapidftr.services.RequestAwareService;
import com.rapidftr.services.RequestCallBackImpl;
import com.rapidftr.services.ScreenCallBack;

public abstract class RequestAwareController extends Controller implements ControllerCallback {
    protected HttpBatchRequestHandler requestHandler;
    protected RequestAwareService service;
    private ScreenCallBack screenCallBack;

    //TODO ST : Refactoring in progress. This constructor will be merged with the one with dispatcher in it.
    public RequestAwareController(CustomScreen screen, UiStack uiStack, RequestAwareService service) {
        super(screen, uiStack);
        this.service = service;
        requestHandler = service.getRequestHandler();
        if (requestHandler != null) {
            ((RequestCallBackImpl) requestHandler.getRequestCallBack()).setScreenCallback((ScreenCallBack) screen);
            ((RequestCallBackImpl) requestHandler.getRequestCallBack()).setControllerCallback(this);
        }
        screen.setController(this);
        screenCallBack = (ScreenCallBack) screen;
    }

    public RequestAwareController(CustomScreen screen, UiStack uiStack, RequestAwareService service, Dispatcher dispatcher) {
        this(screen, uiStack, service);
        this.dispatcher = dispatcher;
    }


    public ScreenCallBack getScreenCallBack() {
        return screenCallBack;
    }

    public void beforeProcessStart() {

    }

}
