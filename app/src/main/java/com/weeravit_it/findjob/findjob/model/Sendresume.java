package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Sendresume {

    @Expose
    private int id;

    @Expose
    @SerializedName("resume_id")
    private Resume resume;

    @Expose
    @SerializedName("jobdetail_id")
    private Jobdetail jobdetail;

    @Expose
    @SerializedName("status_id")
    private Status status;

    public Sendresume(int resumeId, int jobdetailId) {
        this.resume = new Resume(resumeId);
        this.jobdetail = new Jobdetail(jobdetailId);
        this.status = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public Jobdetail getJobdetail() {
        return jobdetail;
    }

    public void setJobdetail(Jobdetail jobdetail) {
        this.jobdetail = jobdetail;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
