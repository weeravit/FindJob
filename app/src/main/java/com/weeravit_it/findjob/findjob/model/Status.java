package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Status {

    @Expose
    private int id;

    @Expose
    private String name;

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
