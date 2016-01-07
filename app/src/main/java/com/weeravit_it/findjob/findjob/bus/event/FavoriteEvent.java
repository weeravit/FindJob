package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.Jobdetail;

import java.util.List;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class FavoriteEvent extends BusEvent {

    public FavoriteEvent() {
    }

    public FavoriteEvent(boolean success) {
        super(success);
    }

    public FavoriteEvent(boolean success, String message) {
        super(success, message);
    }

    public class ShowList extends FavoriteEvent {

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

    public class Check extends FavoriteEvent {

        private boolean checked;

        public Check() {
        }

        public Check(boolean success) {
            super(success);
        }

        public Check(boolean success, String message) {
            super(success, message);
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

    }

    public class Store extends FavoriteEvent {

        public Store() {
        }

        public Store(boolean success) {
            super(success);
        }

        public Store(boolean success, String message) {
            super(success, message);
        }

    }

    public class Delete extends FavoriteEvent {

        private boolean deleted;

        public Delete() {
        }

        public Delete(boolean success) {
            super(success);
        }

        public Delete(boolean success, String message) {
            super(success, message);
        }

        public boolean isDeleted() {
            return deleted;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }

    }

}
