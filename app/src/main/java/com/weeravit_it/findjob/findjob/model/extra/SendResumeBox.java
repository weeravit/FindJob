package com.weeravit_it.findjob.findjob.model.extra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.weeravit_it.findjob.findjob.model.Sendresume;

import java.util.List;

/**
 * Created by Weeravit on 14/10/2558.
 */
public class SendResumeBox {

    @Expose
    @SerializedName("sendresume")
    private List<Sendresume> sendresumes;

    public SendResumeBox(List<Sendresume> sendresumes) {
        this.sendresumes = sendresumes;
    }

    public List<Sendresume> getSendresumes() {
        return sendresumes;
    }

    public void setSendresumes(List<Sendresume> sendresumes) {
        this.sendresumes = sendresumes;
    }

}
