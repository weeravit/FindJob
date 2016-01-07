package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Recent {

    @Expose
    private int id;

    @Expose
    @SerializedName("member_id")
    private Member member;

    @Expose
    @SerializedName("jobdetail_id")
    private Jobdetail jobdetail;

    public Recent(Member member, Jobdetail jobdetail) {
        this.member = member;
        this.jobdetail = jobdetail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Jobdetail getJobdetail() {
        return jobdetail;
    }

    public void setJobdetail(Jobdetail jobdetail) {
        this.jobdetail = jobdetail;
    }

}
