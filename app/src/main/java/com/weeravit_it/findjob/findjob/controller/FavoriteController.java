package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.FavoriteEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Favorite;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class FavoriteController extends BaseController {

    public FavoriteController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void showList(final int page, Member member) {
        Call<JsonData> call = getHttpManager().getAPIService().showFavorites(page, member);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    FavoriteEvent.ShowList showList;
                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<Jobdetail> joblist = getGsonManager().getGson().fromJson(json, new TypeToken<List<Jobdetail>>() {
                        }.getType());
                        showList = new FavoriteEvent().new ShowList(true);
                        showList.setPage(page);
                        showList.setJobdetails(joblist);
                    } else {
                        showList = new FavoriteEvent().new ShowList(false);
                    }

                    getBusProvider().getBus().post(showList);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void check(int jobdetail_id, int member_id) {
        Call<JsonData> call = getHttpManager().getAPIService().checkFavorite(jobdetail_id, member_id);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    FavoriteEvent.Check checkFavoriteEvent;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        checkFavoriteEvent = new FavoriteEvent().new Check(true);
                        checkFavoriteEvent.setChecked(true);
                    } else {
                        checkFavoriteEvent = new FavoriteEvent().new Check(false);
                        checkFavoriteEvent.setChecked(false);
                    }

                    getBusProvider().getBus().post(checkFavoriteEvent);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void store(Member member, Jobdetail jobdetail) {
        Call<JsonData> call = getHttpManager().getAPIService().favoriteStore(
                new Favorite(member, jobdetail)
        );
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    FavoriteEvent.Store store;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        store = new FavoriteEvent().new Store(true);
                    } else {
                        store = new FavoriteEvent().new Store(false);
                    }

                    getBusProvider().getBus().post(store);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void delete(Member member, Jobdetail jobdetail) {
        Call<JsonData> call = getHttpManager().getAPIService().favoriteDelete(
                new Favorite(member, jobdetail)
        );
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    FavoriteEvent.Delete deleteFavoriteEvent;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        deleteFavoriteEvent = new FavoriteEvent().new Delete(true);
                        deleteFavoriteEvent.setDeleted(true);
                    } else {
                        deleteFavoriteEvent = new FavoriteEvent().new Delete(false);
                        deleteFavoriteEvent.setDeleted(false);
                    }

                    getBusProvider().getBus().post(deleteFavoriteEvent);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
