package com.weeravit_it.findjob.findjob.bus;

import android.content.Context;

import de.halfbit.tinybus.TinyBus;

/**
 * Created by Weeravit on 14/10/2558.
 */
public class BusProvider {

    private static BusProvider busProvider;
    private TinyBus tinyBus;

    private BusProvider(Context context) {
        tinyBus = TinyBus.from(context);
    }

    public static BusProvider getInstance(Context context) {
        if (busProvider == null)
            busProvider = new BusProvider(context);
        return busProvider;
    }

    public TinyBus getBus() {
        return tinyBus;
    }

}
