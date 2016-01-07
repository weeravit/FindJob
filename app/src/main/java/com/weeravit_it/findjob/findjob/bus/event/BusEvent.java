package com.weeravit_it.findjob.findjob.bus.event;

/**
 * Created by Weeravit on 14/10/2558.
 */
public class BusEvent {

    private boolean success;
    private String message;

    public BusEvent() {
        this.success = false;
        this.message = null;
    }

    public BusEvent(boolean success) {
        this.success = success;
    }

    public BusEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
