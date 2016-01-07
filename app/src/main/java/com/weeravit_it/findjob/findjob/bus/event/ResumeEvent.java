package com.weeravit_it.findjob.findjob.bus.event;

import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;

import java.util.List;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class ResumeEvent extends BusEvent {

    private ResumeBox resumeBox;

    public ResumeEvent() {
    }

    public ResumeEvent(boolean success) {
        super(success);
    }

    public ResumeEvent(boolean success, String message) {
        super(success, message);
    }

    public ResumeBox getResumeBox() {
        return resumeBox;
    }

    public void setResumeBox(ResumeBox resumeBox) {
        this.resumeBox = resumeBox;
    }

    public class ShowList extends ResumeEvent {

        private int page;
        private List<ResumeBox> resumeBoxes;

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

        public List<ResumeBox> getResumeBoxes() {
            return resumeBoxes;
        }

        public void setResumeBoxes(List<ResumeBox> resumeBoxes) {
            this.resumeBoxes = resumeBoxes;
        }
    }

    public class Show extends ResumeEvent {

        public Show() {
        }

        public Show(boolean success) {
            super(success);
        }

        public Show(boolean success, String message) {
            super(success, message);
        }

    }

    public class Store extends ResumeEvent {

        public Store() {
        }

        public Store(boolean success) {
            super(success);
        }

        public Store(boolean success, String message) {
            super(success, message);
        }

    }

    public class Update extends ResumeEvent {
        public Update() {
        }

        public Update(boolean success) {
            super(success);
        }

        public Update(boolean success, String message) {
            super(success, message);
        }
    }

    public class Delete extends ResumeEvent {
        public Delete() {
        }

        public Delete(boolean success) {
            super(success);
        }

        public Delete(boolean success, String message) {
            super(success, message);
        }
    }

    public class Reload extends ResumeEvent {

    }

}
