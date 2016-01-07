package com.weeravit_it.findjob.findjob.utils;

import android.content.Context;

/**
 * Created by Weeravit on 15/10/2558.
 */
public class Resource {

    private static Resource resource;
    private Context context;

    private Resource() {

    }

    public static Resource getInstance() {
        if (resource == null)
            resource = new Resource();
        return resource;
    }

}
