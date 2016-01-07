package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.Jobdetail;

import java.util.List;

/**
 * Created by Weeravit on 16/10/2558.
 */
public class RecentEvent extends BusEvent {

    public RecentEvent() {
    }

    public RecentEvent(boolean success) {
        super(success);
    }

    public RecentEvent(boolean success, String message) {
        super(success, message);
    }

    public class ShowList extends RecentEvent {

        private int page;
        private List<Jobdetail> jobdetails;

        public ShowList() {
        }

        public ShowList(boolean success) {
            super(success);
        }

        public ShowList(boolean success, String message) {
            super(success, message);
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<Jobdetail> getJobdetails() {
            return jobdetails;
        }

        public void setJobdetails(List<Jobdetail> jobdetails) {
            this.jobdetails = jobdetails;
        }

    }

    public class Store extends RecentEvent {

        public Store() {
        }

        public Store(boolean success) {
            super(success);
        }

        public Store(boolean success, String message) {
            super(success, message);
        }

    }

}
