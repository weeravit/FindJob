package com.weeravit_it.findjob.findjob.controller;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.RequestBody;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.ResumeEvent;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.Resume;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;
import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class ResumeController extends BaseController {

    public ResumeController(Context context, BusProvider busProvider, HTTPManager httpManager, GsonManager gsonManager) {
        super(context, busProvider, httpManager, gsonManager);
    }

    public void showList(final int page, Member member) {
        Call<JsonData> call = getHttpManager().getAPIService().showResumes(page, member);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    ResumeEvent.ShowList showList;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        List<ResumeBox> joblist = getGsonManager().getGson().fromJson(json, new TypeToken<List<ResumeBox>>(){}.getType());
                        showList = new ResumeEvent().new ShowList(true);
                        showList.setPage(page);
                        showList.setResumeBoxes(joblist);
                    } else {
                        showList = new ResumeEvent().new ShowList(false);
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
        Call<JsonData> call = getHttpManager().getAPIService().showResume(new Resume(id));
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    ResumeEvent.Show show;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
                        ResumeBox resumeBox = getGsonManager().getGson().fromJson(json, ResumeBox.class);
                        show = new ResumeEvent().new Show(true);
                        show.setResumeBox(resumeBox);
                    } else {
                        show = new ResumeEvent().new Show(false);
                    }

                    getBusProvider().getBus().post(show);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void store(RequestBody image, RequestBody resume) {
        Call<JsonData> call = getHttpManager().getAPIService().resumeStore(image, resume);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    ResumeEvent.Store store;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
//                        String json = response.body().getResults().toString();
                        ResumeBox resumeBox = getGsonManager().getGson().fromJson(json, ResumeBox.class);
                        store = new ResumeEvent().new Store(true);
                        store.setResumeBox(resumeBox);
                    } else {
                        store = new ResumeEvent().new Store(false);
                    }

                    getBusProvider().getBus().post(store);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void update(RequestBody image, RequestBody resume) {
        Call<JsonData> call = getHttpManager().getAPIService().resumeUpdate(image, resume);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    ResumeEvent.Update update;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        String json = getGsonManager().getGson().toJson(response.body().getResults());
//                        String json = response.body().getResults().toString();
                        ResumeBox resumeBox = getGsonManager().getGson().fromJson(json, ResumeBox.class);
                        update = new ResumeEvent().new Update(true);
                        update.setResumeBox(resumeBox);
                    } else {
                        update = new ResumeEvent().new Update(false);
                    }

                    getBusProvider().getBus().post(update);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    public void delete(ResumeBox resumeBox) {
        Call<JsonData> call = getHttpManager().getAPIService().resumeDelete(resumeBox);
        call.enqueue(new Callback<JsonData>() {
            @Override
            public void onResponse(Response<JsonData> response, Retrofit retrofit) {
                if (getContext() != null) {
                    ResumeEvent.Delete delete;

                    if (response.body().getStatus().equals(getContext().getString(R.string.status_success))) {
                        delete = new ResumeEvent().new Delete(true);
                    } else {
                        delete = new ResumeEvent().new Delete(false);
                    }

                    getBusProvider().getBus().post(delete);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
