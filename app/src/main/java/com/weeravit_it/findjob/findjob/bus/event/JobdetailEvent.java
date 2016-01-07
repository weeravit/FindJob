package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.Jobdetail;

import java.util.List;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class JobdetailEvent extends BusEvent {

    public JobdetailEvent() {
    }

    public JobdetailEvent(boolean success) {
        super(success);
    }

    public JobdetailEvent(boolean success, String message) {
        super(success, message);
    }

    public class ShowList extends JobdetailEvent {

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

    public class ShowListNearBy extends JobdetailEvent {

        private int page;
        private List<Jobdetail> jobdetails;

        public ShowListNearBy() {
        }

        public ShowListNearBy(boolean success) {
            super(success);
        }

        public ShowListNearBy(boolean success, String message) {
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

    public class Show extends JobdetailEvent {

        private Jobdetail jobdetail;

        public Show() {
        }

        public Show(boolean success) {
            super(success);
        }

        public Show(boolean success, String message) {
            super(success, message);
        }

        public Jobdetail getJobdetail() {
            return jobdetail;
        }

        public void setJobdetail(Jobdetail jobdetail) {
            this.jobdetail = jobdetail;
        }
    }

}
