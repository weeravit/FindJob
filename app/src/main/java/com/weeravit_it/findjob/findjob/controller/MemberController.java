package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.MemberEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 14/10/2558.
 */
public class MemberController extends BaseController {

    public MemberController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void login(Member member) {
        Call<JsonData> call = getHttpManager().getAPIService().login(member);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    MemberEvent.Login loginEvent;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        Member member = getGsonManager().getGson().fromJson(json, Member.class);
                        LocalStorage.getInstance().setMember(member);
                        loginEvent = new MemberEvent().new Login(true); // success
                    } else {
                        loginEvent = new MemberEvent().new Login(false); // fail
                    }

                    getBusProvider().getBus().post(loginEvent);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void register(Member member) {
        Call<JsonData> call = getHttpManager().getAPIService().register(member);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    MemberEvent.Register registerEvent;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        Member member = getGsonManager().getGson().fromJson(json, Member.class);
                        LocalStorage.getInstance().setMember(member);
                        registerEvent = new MemberEvent().new Register(true);
                    } else {
                        registerEvent = new MemberEvent().new Register(false);
                    }

                    getBusProvider().getBus().post(registerEvent);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void update(Member member) {
        Call<JsonData> call = getHttpManager().getAPIService().profileUpdate(member);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    MemberEvent.Update update;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        Member member = getGsonManager().getGson().fromJson(json, Member.class);
                        LocalStorage.getInstance().setMember(member);
                        update = new MemberEvent().new Update(true);
                    } else {
                        update = new MemberEvent().new Update(false);
                    }

                    getBusProvider().getBus().post(update);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
