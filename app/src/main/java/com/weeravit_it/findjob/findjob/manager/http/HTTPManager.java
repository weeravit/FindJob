package com.weeravit_it.findjob.findjob.manager.http;

import com.google.gson.Gson;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class HTTPManager {

    private static HTTPManager httpManager;
    private final String LOCAL_SERVER = "http://10.0.3.2:8080/findjob";
//    private final String HOST_SERVER = "http://findjob.weeravit-it.com";
    private final String HOST_SERVER = "http://findjob.uinno.co.th/findjob";
    private final String API_SERVICE = HOST_SERVER + "/web-service/";

    private Retrofit retrofit;
    private APIService APIService;

    private HTTPManager() {
        createAPI();
    }

    private void createAPI() {
        Gson gson = GsonManager.getInstance().getGson();

        retrofit = new Retrofit.Builder()
                .baseUrl(API_SERVICE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        APIService = retrofit.create(APIService.class);
    }

    public static HTTPManager getInstance() {
        if(httpManager == null)
            httpManager = new HTTPManager();
        return httpManager;
    }

    public APIService getAPIService() {
        return APIService;
    }

}
