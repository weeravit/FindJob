package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Operator {

    @Expose
    private int id;

    @Expose
    private String name;

    @Expose
    private String address;

    @Expose
    private String tel;

    @Expose
    @SerializedName("gps_lat")
    private String lat;

    @Expose
    @SerializedName("gps_lng")
    private String lng;

    @Expose
    @SerializedName("image")
    private String imageUrl;

    @Expose
    private String registration;

    @Expose
    @SerializedName("member_id")
    private Member member;

    @Expose
    @SerializedName("district_id")
    private District district;

    @Expose
    @SerializedName("status_id")
    private Status status;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
