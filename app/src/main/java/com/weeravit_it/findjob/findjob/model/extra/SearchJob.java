package com.weeravit_it.findjob.findjob.model.extra;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.weeravit_it.findjob.findjob.model.Categoryjob;
import com.weeravit_it.findjob.findjob.model.District;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.Province;

/**
 * Created by Weeravit on 25/8/2558.
 */
public class SearchJob {

    private static SearchJob searchJob;

    @Expose
    private String notice;

    @Expose
    private Job job;

    @Expose
    private Categoryjob categoryjob;

    @Expose
    private District district;

    @Expose
    private Province province;

    @Expose
    private String salary;

    @Expose
    private LatLng latLng;

    public static SearchJob getInstance() {
        if(searchJob == null)
            searchJob = new SearchJob();
        return searchJob;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public Categoryjob getCategoryjob() {
        return categoryjob;
    }

    public void setCategoryjob(Categoryjob categoryjob) {
        this.categoryjob = categoryjob;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void clear() {
        this.notice = null;
        this.job = null;
        this.categoryjob = null;
        this.district = null;
        this.province = null;
        this.salary = null;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
