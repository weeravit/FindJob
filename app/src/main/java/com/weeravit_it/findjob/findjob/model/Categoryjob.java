package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Categoryjob {

    @Expose
    private int id;

    @Expose
    private String name;

    public Categoryjob(int id) {
        this.id = id;
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

}
