package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.JobEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class JobController extends BaseController {

    public JobController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void shows(final int page, String message) {
        Call<JsonData> call = getHttpManager().getAPIService().showJobs(page, message);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    JobEvent.ShowJobs showJobs;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Job> jobs = getGsonManager().getGson().fromJson(json, new TypeToken<List<Job>>(){}.getType());

                        showJobs = new JobEvent().new ShowJobs(true);
                        showJobs.setPage(page);
                        showJobs.setJobs(jobs);
                    } else {
                        showJobs = new JobEvent().new ShowJobs(false);
                    }

                    getBusProvider().getBus().post(showJobs);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
