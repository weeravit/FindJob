package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.extra.SearchResult;

import java.util.List;

/**
 * Created by Weeravit on 16/10/2558.
 */
public class SearchEvent extends BusEvent {

    private int page;

    public SearchEvent() {
    }

    public SearchEvent(boolean success) {
        super(success);
    }

    public SearchEvent(boolean success, String message) {
        super(success, message);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public class Job extends SearchEvent {

        private String msgSearch;
        private List<SearchResult> searchResults;

        public Job() {
        }

        public Job(boolean success) {
            super(success);
        }

        public Job(boolean success, String message) {
            super(success, message);
        }

        public String getMsgSearch() {
            return msgSearch;
        }

        public void setMsgSearch(String msgSearch) {
            this.msgSearch = msgSearch;
        }

        public List<SearchResult> getSearchResults() {
            return searchResults;
        }

        public void setSearchResults(List<SearchResult> searchResults) {
            this.searchResults = searchResults;
        }

    }

    public class Place extends SearchEvent {

        private String msgSearch;
        private List<SearchResult> searchResults;

        public Place() {
        }

        public Place(boolean success) {
            super(success);
        }

        public Place(boolean success, String message) {
            super(success, message);
        }

        public String getMsgSearch() {
            return msgSearch;
        }

        public void setMsgSearch(String msgSearch) {
            this.msgSearch = msgSearch;
        }

        public List<SearchResult> getSearchResults() {
            return searchResults;
        }

        public void setSearchResults(List<SearchResult> searchResults) {
            this.searchResults = searchResults;
        }

    }

    public class Jobdetail extends SearchEvent {

        private List<com.weeravit_it.findjob.findjob.model.Jobdetail> jobdetails;

        public Jobdetail() {
        }

        public Jobdetail(boolean success) {
            super(success);
        }

        public Jobdetail(boolean success, String message) {
            super(success, message);
        }

        public List<com.weeravit_it.findjob.findjob.model.Jobdetail> getJobdetails() {
            return jobdetails;
        }

        public void setJobdetails(List<com.weeravit_it.findjob.findjob.model.Jobdetail> jobdetails) {
            this.jobdetails = jobdetails;
        }

    }

}
