package com.weeravit_it.findjob.findjob.bus.event;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class MemberEvent extends BusEvent {

    public MemberEvent() {
    }

    public MemberEvent(boolean success) {
        super(success);
    }

    public MemberEvent(boolean success, String message) {
        super(success, message);
    }

    public class Login extends MemberEvent {

        public Login() {
        }

        public Login(boolean success) {
            super(success);
        }

        public Login(boolean success, String message) {
            super(success, message);
        }

    }

    public class Register extends MemberEvent {

        public Register() {
        }

        public Register(boolean success) {
            super(success);
        }

        public Register(boolean success, String message) {
            super(success, message);
        }

    }

    public class Update extends MemberEvent {

        public Update() {
        }

        public Update(boolean success) {
            super(success);
        }

        public Update(boolean success, String message) {
            super(success, message);
        }

    }

    public class Reload extends MemberEvent {

    }

}
