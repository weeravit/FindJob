package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;

/**
 * Created by Weeravit on 14/10/2558.
 */
public class BaseController {

    private Context context;
    private BusProvider busProvider;
    private HTTPManager httpManager;
    private GsonManager gsonManager;

    public BaseController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        this.context = context;
        this.busProvider = busProvider;
        this.httpManager = httpManager;
        this.gsonManager = gsonManager;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public BusProvider getBusProvider() {
        return busProvider;
    }

    public void setBusProvider(BusProvider busProvider) {
        this.busProvider = busProvider;
    }

    public HTTPManager getHttpManager() {
        return httpManager;
    }

    public void setHttpManager(HTTPManager httpManager) {
        this.httpManager = httpManager;
    }

    public GsonManager getGsonManager() {
        return gsonManager;
    }

    public void setGsonManager(GsonManager gsonManager) {
        this.gsonManager = gsonManager;
    }

}
