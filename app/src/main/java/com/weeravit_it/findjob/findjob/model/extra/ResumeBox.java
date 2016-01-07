package com.weeravit_it.findjob.findjob.model.extra;

import com.google.gson.annotations.Expose;
import com.weeravit_it.findjob.findjob.model.Resume;
import com.weeravit_it.findjob.findjob.model.Resumejob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weeravit on 6/10/2558.
 */
public class ResumeBox {

    @Expose
    private Resume resume;

    @Expose
    private List<Resumejob> resumejobs;

    @Expose
    private String image;

    public ResumeBox() {
        resume = new Resume();
        resumejobs = new ArrayList<>();
        image = null;
    }

    public ResumeBox(Resume resume, List<Resumejob> resumejobs) {
        this.resume = resume;
        this.resumejobs = resumejobs;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public List<Resumejob> getResumejobs() {
        return resumejobs;
    }

    public void setResumejobs(List<Resumejob> resumejobs) {
        this.resumejobs = resumejobs;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
