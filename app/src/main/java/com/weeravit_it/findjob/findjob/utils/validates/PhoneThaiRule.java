package com.weeravit_it.findjob.findjob.utils.validates;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.lang.annotation.Annotation;

/**
 * Created by Weeravit on 22/12/2558.
 */
public class PhoneThaiRule extends AnnotationRule<PhoneThai, String> {

    /**
     * Constructor. It is mandatory that all subclasses MUST have a constructor with the same
     * signature.
     *
     * @param phoneThai The rule {@link Annotation} instance to which
     *            this rule is paired.
     */
    protected PhoneThaiRule(PhoneThai phoneThai) {
        super(phoneThai);
    }

    @Override
    public boolean isValid(String s) {
        boolean isVailid = false;
//        if (s.length() >= mRuleAnnotation.min() && s.length() <= mRuleAnnotation.max()) {
            if (!s.substring(0, 2).equals("00") && !s.substring(0, 2).equals("01") && s.substring(0,1).equals("0")) {
                isVailid = true;
            }
//        }
        return isVailid;
    }

}
