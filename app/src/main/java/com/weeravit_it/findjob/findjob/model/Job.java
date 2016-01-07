package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Job {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    @SerializedName("categoryjob_id")
    private Categoryjob categoryjob;

    public Job(int id) {
        this.id = id;
    }

    public Job(int id, String name) {
        this.id = id;
        this.name = name;
        this.categoryjob = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Categoryjob getCategoryjob() {
        return categoryjob;
    }

    public void setCategoryjob(Categoryjob categoryjob) {
        this.categoryjob = categoryjob;
    }

}
