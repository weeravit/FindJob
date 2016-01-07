package com.weeravit_it.findjob.findjob.model.extra;

import com.google.gson.annotations.Expose;

/**
 * Created by Weeravit on 21/8/2558.
 */
public class SearchResult {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String tag;

    private boolean checked;

    public SearchResult(String name, String tag) {
        this.name = name;
        this.tag = tag;
    }

    public SearchResult(int id, String name) {
        this.id = id;
        this.name = name;
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

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
