package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.Job;

import java.util.List;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class JobEvent extends BusEvent {

    public JobEvent() {
    }

    public JobEvent(boolean success) {
        super(success);
    }

    public JobEvent(boolean success, String message) {
        super(success, message);
    }

    public class ShowJobs extends JobEvent {

        private int page;
        private List<Job> jobs;

        public ShowJobs(boolean success) {
            super(success);
        }

        public ShowJobs(boolean success, String message) {
            super(success, message);
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<Job> getJobs() {
            return jobs;
        }

        public void setJobs(List<Job> jobs) {
            this.jobs = jobs;
        }

    }
}
