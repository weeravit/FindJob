package com.weeravit_it.findjob.findjob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Resume {

    @Expose
    private int id;

    @Expose
    private String objective;

    @Expose
    private String experience;

    @Expose
    private String education;

    @Expose
    private String skill;

    @Expose
    @SerializedName("member_id")
    private Member member;

    public Resume() {
    }

    public Resume(int id) {
        this.id = id;
    }

    public Resume(String objective, String experience, String education, String skill, Member member) {
        this.objective = objective;
        this.experience = experience;
        this.education = education;
        this.skill = skill;
        this.member = member;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        if (objective == null)
            objective = "";
        this.objective = objective;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        if (experience == null)
            experience = "";
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        if (education == null)
            education = "";
        this.education = education;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        if (skill == null)
            skill = "";
        this.skill = skill;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

}
