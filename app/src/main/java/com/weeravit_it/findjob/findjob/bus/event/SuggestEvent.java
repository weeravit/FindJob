package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.Jobsuggest;

import java.util.List;

/**
 * Created by Weeravit on 16/10/2558.
 */
public class SuggestEvent extends BusEvent {

    public SuggestEvent() {
    }

    public SuggestEvent(boolean success) {
        super(success);
    }

    public SuggestEvent(boolean success, String message) {
        super(success, message);
    }

    public class ShowList extends SuggestEvent {

        private int page;
        private List<Jobsuggest> jobsuggests;

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

        public List<Jobsuggest> getJobsuggests() {
            return jobsuggests;
        }

        public void setJobsuggests(List<Jobsuggest> jobsuggests) {
            this.jobsuggests = jobsuggests;
        }

    }

}
