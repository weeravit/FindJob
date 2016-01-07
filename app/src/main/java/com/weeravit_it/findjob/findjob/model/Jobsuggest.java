package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Jobsuggest {

    @Expose
    private int id;

    @Expose
    @SerializedName("jobdetail_id")
    private Jobdetail jobdetail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Jobdetail getJobdetail() {
        return jobdetail;
    }

    public void setJobdetail(Jobdetail jobdetail) {
        this.jobdetail = jobdetail;
    }

}
