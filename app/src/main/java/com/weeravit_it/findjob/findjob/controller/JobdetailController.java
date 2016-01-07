package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.JobdetailEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class JobdetailController extends BaseController {

    public JobdetailController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void showList(final int page) {
        Call<JsonData> call = getHttpManager().getAPIService().feedJobdetail(page);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    JobdetailEvent.ShowList showList;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Jobdetail> jobdetails = getGsonManager().getGson().fromJson(json, new TypeToken<List<Jobdetail>>(){}.getType());

                        showList = new JobdetailEvent().new ShowList(true);
                        showList.setPage(page);
                        showList.setJobdetails(jobdetails);
                    } else {
                        showList = new JobdetailEvent().new ShowList(false);
                    }

                    getBusProvider().getBus().post(showList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void showListNearBy(final int page, double latitude, double longitude, int distance) {
        Call<JsonData> call = getHttpManager().getAPIService().nearJobdetail(
                page,
                latitude,
                longitude,
                distance
        );
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    JobdetailEvent.ShowListNearBy showList;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Jobdetail> jobdetails = getGsonManager().getGson().fromJson(json, new TypeToken<List<Jobdetail>>(){}.getType());

                        showList = new JobdetailEvent().new ShowListNearBy(true);
                        showList.setPage(page);
                        showList.setJobdetails(jobdetails);
                    } else {
                        showList = new JobdetailEvent().new ShowListNearBy(false);
                    }

                    getBusProvider().getBus().post(showList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void show(int id) {
        Call<JsonData> call = getHttpManager().getAPIService().showJobdetail(id);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    JobdetailEvent.Show showJobdetailEvent;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        Jobdetail jobdetail = getGsonManager().getGson().fromJson(json, Jobdetail.class);

                        showJobdetailEvent = new JobdetailEvent().new Show(true);
                        showJobdetailEvent.setJobdetail(jobdetail);
                    } else {
                        showJobdetailEvent = new JobdetailEvent().new Show(false);
                    }

                    getBusProvider().getBus().post(showJobdetailEvent);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
