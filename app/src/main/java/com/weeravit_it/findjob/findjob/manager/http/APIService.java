package com.weeravit_it.findjob.findjob.manager.http;

import com.squareup.okhttp.RequestBody;
import com.weeravit_it.findjob.findjob.model.Favorite;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Jobsuggest;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.Recent;
import com.weeravit_it.findjob.findjob.model.Resume;
import com.weeravit_it.findjob.findjob.model.extra.JsonData;
import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;
import com.weeravit_it.findjob.findjob.model.extra.SearchJob;
import com.weeravit_it.findjob.findjob.model.extra.SearchResult;
import com.weeravit_it.findjob.findjob.model.extra.SendResumeBox;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Weeravit on 20/8/2558.
 */
public interface APIService {

    // Jobdetail
    @GET("jobdetails/feed")
    public Call<JsonData> feedJobdetail(@Query("page") int page);

    @GET("jobdetails/near")
    public Call<JsonData> nearJobdetail(@Query("page") int page, @Query("lat") double lat, @Query("lng") double lng, @Query("distance") int distance);

    @GET("jobdetails/show/{id}")
    public Call<JsonData> showJobdetail(@Path("id") int id);

    // Search
    @GET("search/job")
    public Call<JsonData> searchJob(@Query("page") int page, @Query("message") String message);

    @GET("search/place")
    public Call<JsonData> searchPlace(@Query("page") int page, @Query("message") String message);

    @POST("search/jobdetail")
    public Call<JsonData> searchJobdetail(@Query("page") int page, @Body SearchJob searchJob);

    // Job Suggest
    @GET("jobsuggests")
    public Call<JsonData> jobSuggest(@Query("page") int page);

    // Member
    @POST("members/login")
    public Call<JsonData> login(@Body Member member);

    @POST("members/register")
    public Call<JsonData> register(@Body Member member);

    @POST("members/update")
    public Call<JsonData> profileUpdate(@Body Member member);

    // Recent
    @POST("recents/show")
    public Call<JsonData> recentShow(@Query("page") int page, @Body Member member);

    @POST("recents/store")
    public Call<JsonData> recentStore(@Body Recent recent);

    // Favorite
    @GET("favorite/jobdetail/{jobdetail_id}/member/{member_id}")
    public Call<JsonData> checkFavorite(@Path("jobdetail_id") int jobdetail_id, @Path("member_id") int member_id);

    @POST("favorites/show")
    public Call<JsonData> showFavorites(@Query("page") int page, @Body Member member);

    @POST("favorites/store")
    public Call<JsonData> favoriteStore(@Body Favorite favorite);

    @POST("favorites/delete")
    public Call<JsonData> favoriteDelete(@Body Favorite favorite);

    // Resume
    @POST("resumes/member")
    public Call<JsonData> showResumes(@Query("page") int page, @Body Member member);

    @POST("resume/member")
    public Call<JsonData> showResume(@Body Resume resume);

    @Multipart
    @POST("resume/store")
    public Call<JsonData> resumeStore(@Part("image\"; filename=\"pp.png\" ") RequestBody image, @Part("resume") RequestBody resume);

    @Multipart
    @POST("resume/update")
    public Call<JsonData> resumeUpdate(@Part("image\"; filename=\"pp.png\" ") RequestBody image, @Part("resume") RequestBody resume);

    @POST("resume/delete")
    public Call<JsonData> resumeDelete(@Body ResumeBox resume);

    @POST("jobs")
    public Call<JsonData> showJobs(@Query("page") int page, @Query("message") String message);

    @POST("sendresume/store")
    public Call<JsonData> sendResumeStore(@Body SendResumeBox sendResumeBox);

}
