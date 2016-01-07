package com.weeravit_it.findjob.findjob.manager.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class GsonManager {

    private static GsonManager GsonManager;
    private Gson gson;

    private GsonManager() {
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public static GsonManager getInstance() {
        if(GsonManager == null)
            GsonManager = new GsonManager();
        return GsonManager;
    }

    public Gson getGson() {
        return gson;
    }

}
