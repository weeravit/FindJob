package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.SearchEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;
import com.weeravit_it.findjob.findjob.model.extra.SearchJob;
import com.weeravit_it.findjob.findjob.model.extra.SearchResult;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 16/10/2558.
 */
public class SearchController extends BaseController {

    public SearchController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void showListJob(final int page, String message) {
        Call<JsonData> call = getHttpManager().getAPIService().searchJob(page, message);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    SearchEvent.Job job;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<SearchResult> searchResults = getGsonManager().getGson().fromJson(json, new TypeToken<List<SearchResult>>(){}.getType());

                        job = new SearchEvent().new Job(true);
                        job.setPage(page);
                        job.setSearchResults(searchResults);
                    } else {
                        job = new SearchEvent().new Job(false);
                    }

                    getBusProvider().getBus().post(job);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void showListPlace(final int page, String message) {
        Call<JsonData> call = getHttpManager().getAPIService().searchPlace(page, message);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    SearchEvent.Place place;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<SearchResult> searchResults = getGsonManager().getGson().fromJson(json, new TypeToken<List<SearchResult>>(){}.getType());

                        place = new SearchEvent().new Place(true);
                        place.setPage(page);
                        place.setSearchResults(searchResults);
                    } else {
                        place = new SearchEvent().new Place(false);
                    }

                    getBusProvider().getBus().post(place);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void showListJobdetail(final int page, SearchJob searchJob) {
        Call<JsonData> call = getHttpManager().getAPIService().searchJobdetail(page, searchJob);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    SearchEvent.Jobdetail jobdetail;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Jobdetail> jobdetails = getGsonManager().getGson().fromJson(json, new TypeToken<List<Jobdetail>>(){}.getType());

                        jobdetail = new SearchEvent().new Jobdetail(true);
                        jobdetail.setPage(page);
                        jobdetail.setJobdetails(jobdetails);
                    } else {
                        jobdetail = new SearchEvent().new Jobdetail(false);
                    }

                    getBusProvider().getBus().post(jobdetail);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
