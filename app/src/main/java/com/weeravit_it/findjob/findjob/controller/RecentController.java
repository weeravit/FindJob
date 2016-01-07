package com.weeravit_it.findjob.findjob.controller;

import android.app.Activity;
import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.RecentEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.Recent;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 16/10/2558.
 */
public class RecentController extends BaseController {

    public RecentController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void showList(final int page, Member member) {
        Call<JsonData> call = getHttpManager().getAPIService().recentShow(page, member);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {

                    RecentEvent.ShowList showList;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Jobdetail> joblist = getGsonManager().getGson().fromJson(json, new TypeToken<List<Jobdetail>>() {
                        }.getType());
                        showList = new RecentEvent().new ShowList(true);
                        showList.setPage(page);
                        showList.setJobdetails(joblist);
                    } else {
                        showList = new RecentEvent().new ShowList(false);
                    }

                    getBusProvider().getBus().post(showList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void store(Recent recent) {
        Call<JsonData> call = getHttpManager().getAPIService().recentStore(recent);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
