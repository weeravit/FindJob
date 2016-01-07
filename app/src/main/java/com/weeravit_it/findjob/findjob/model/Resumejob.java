package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Resumejob {

    @Expose
    private int id;

    @Expose
    @SerializedName("resume_id")
    private Resume resume;

    @Expose
    @SerializedName("job_id")
    private Job job;

    public Resumejob(Job job) {
        this.job = job;
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

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

}
