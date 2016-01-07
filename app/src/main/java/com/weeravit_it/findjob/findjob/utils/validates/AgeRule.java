package com.weeravit_it.findjob.findjob.utils.validates;

import com.mobsandgeeks.saripaar.AnnotationRule;

import java.lang.annotation.Annotation;

/**
 * Created by Weeravit on 22/12/2558.
 */
public class AgeRule extends AnnotationRule<Age, Integer> {

    /**
     * Constructor. It is mandatory that all subclasses MUST have a constructor with the same
     * signature.
     *
     * @param age The rule {@link Annotation} instance to which
     *            this rule is paired.
     */
    protected AgeRule(Age age) {
        super(age);
    }

    @Override
    public boolean isValid(Integer integer) {
        return (integer >= mRuleAnnotation.min() && integer <= mRuleAnnotation.max());
    }

}
