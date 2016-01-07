package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.SuggestEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Jobsuggest;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 16/10/2558.
 */
public class SuggestController extends BaseController {

    public SuggestController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void showList(final int page) {
        Call<JsonData> call = getHttpManager().getAPIService().jobSuggest(page);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    SuggestEvent.ShowList showList;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Jobsuggest> jobsuggests = getGsonManager().getGson().fromJson(json, new TypeToken<List<Jobsuggest>>(){}.getType());

                        showList = new SuggestEvent().new ShowList(true);
                        showList.setPage(page);
                        showList.setJobsuggests(jobsuggests);
                    } else {
                        showList = new SuggestEvent().new ShowList(false);
                    }

                    getBusProvider().getBus().post(showList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
