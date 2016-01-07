package com.weeravit_it.findjob.findjob.utils.validates;

import com.mobsandgeeks.saripaar.annotation.ValidateUsing;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Weeravit on 22/12/2558.
 */
@ValidateUsing(AgeRule.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Age {

    int sequence()          default -1;
    int messageResId()      default -1;
    String message()        default "This field is required";
    int min()           default 7;
    int max()           default 60;

}
