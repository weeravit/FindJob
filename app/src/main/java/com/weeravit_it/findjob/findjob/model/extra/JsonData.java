package com.weeravit_it.findjob.findjob.model.extra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 2/9/2558.
 */
public class JsonData {

    @Expose
    @SerializedName("results")
    private Object results;

    @Expose
    @SerializedName("status")
    private String status;

    public JsonData(Object results) {
        this.results = results;
        this.status = null;
    }

    public Object getResults() {
        return results;
    }

    public void setResults(Object results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
