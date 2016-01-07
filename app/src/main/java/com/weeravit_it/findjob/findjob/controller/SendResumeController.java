package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.SendResumeEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Sendresume;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;
import com.weeravit_it.findjob.findjob.model.extra.SendResumeBox;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class SendResumeController extends BaseController {

    public SendResumeController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void send(List<Sendresume> sendresumes) {
        SendResumeBox jsonData = new SendResumeBox(sendresumes);
        String json = getGsonManager().getGson().toJson(jsonData);
        Call<JsonData> call = getHttpManager().getAPIService().sendResumeStore(jsonData);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    SendResumeEvent.Send send;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        send = new SendResumeEvent().new Send(true);
                    } else {
                        send = new SendResumeEvent().new Send(false);
                    }

                    getBusProvider().getBus().post(send);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
