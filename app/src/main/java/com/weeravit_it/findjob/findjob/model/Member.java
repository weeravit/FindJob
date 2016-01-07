package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weeravit_it.findjob.findjob.utils.Hash;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Member {

    @Expose
    private int id;

    @Expose
    private String firstname;

    @Expose
    private String lastname;

    @Expose
    private String address;

    @Expose
    private int age;

    @Expose
    private String tel;

    @Expose
    private String email;

    @Expose
    private String pass;

    @Expose
    @SerializedName("categorymember_id")
    private Categorymember categorymember;

    @Expose
    @SerializedName("status_id")
    private Status status;

    public Member(String email, String password) {
        this.email = email;
        setPass(password);
    }

    public Member(String email, String password, String telephoneNumber) {
        this.email = email;
        setPass(password);
        this.tel = telephoneNumber;
    }

    public Member(String firstname, String lastname, String address, int age, String tel, String email, String pass) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
        this.age = age;
        this.tel = tel;
        this.email = email;
        setPass(pass);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        try {
            pass = Hash.md5(pass);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        this.pass = pass;
    }

    public Categorymember getCategorymember() {
        return categorymember;
    }

    public void setCategorymember(Categorymember categorymember) {
        this.categorymember = categorymember;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
