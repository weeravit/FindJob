package com.weeravit_it.findjob.findjob.utils.validates;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Weeravit on 22/12/2558.
 */
@ValidateUsing(PhoneThaiRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PhoneThai {

    int sequence()          default -1;
    int messageResId()      default -1;
    String message()        default "This No.Phone not start 00, 01";

}
