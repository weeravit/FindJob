package com.weeravit_it.findjob.findjob.utils;

import android.content.Context;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class Contextor {

    private static Contextor contextor;
    private Context context;

    private Contextor() {
    }

    public static Contextor getInstance() {
        if(contextor == null)
            contextor = new Contextor();
        return contextor;
    }

    public void setMainContext(Context context) {
        this.context = context;
    }

    public Context getMainContext() {
        return context;
    }

}
