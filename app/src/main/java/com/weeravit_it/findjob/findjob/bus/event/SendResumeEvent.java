package com.weeravit_it.findjob.findjob.bus.event;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class SendResumeEvent extends BusEvent {

    public SendResumeEvent() {
    }

    public SendResumeEvent(boolean success) {
        super(success);
    }

    public SendResumeEvent(boolean success, String message) {
        super(success, message);
    }

    public class Send extends  SendResumeEvent {
        public Send() {
        }

        public Send(boolean success) {
            super(success);
        }

        public Send(boolean success, String message) {
            super(success, message);
        }
    }

}
