package com.weeravit_it.findjob.findjob.model.extra;

import com.weeravit_it.findjob.findjob.model.Job;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Weeravit on 9/10/2558.
 */
public class SelectedJobs {

    private static SelectedJobs selectedJobs;
    private List<Job> jobs;

    private SelectedJobs(){
        jobs = new ArrayList<>();
    }

    public static SelectedJobs getInstance() {
        if (selectedJobs == null)
            selectedJobs = new SelectedJobs();
        return selectedJobs;
    }

    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public boolean equal(Job job) {
        Iterator<Job> jobIterator = jobs.iterator();
        while (jobIterator.hasNext()) {
            Job selectedJob = jobIterator.next();
            if (selectedJob.getId() == job.getId())
                return true;
        }
        return false;
    }

    public void remove(Job job) {
        Iterator<Job> jobIterator = jobs.iterator();
        while (jobIterator.hasNext()) {
            Job selectedJob = jobIterator.next();
            if (selectedJob.getId() == job.getId()) {
                jobIterator.remove();
                return;
            }
        }
    }

}
