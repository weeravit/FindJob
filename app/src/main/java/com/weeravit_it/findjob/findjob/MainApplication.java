package com.weeravit_it.findjob.findjob;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.mobsandgeeks.saripaar.Validator;
import com.weeravit_it.findjob.findjob.utils.Contextor;
import com.weeravit_it.findjob.findjob.utils.validates.Age;
import com.weeravit_it.findjob.findjob.utils.validates.PhoneThai;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Weeravit on 20/8/2558.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        Iconify.with(new FontAwesomeModule());
//                .with(new MaterialModule());

        Contextor.getInstance().setMainContext(MainApplication.this);

        registerValidator();
    }

    private void registerValidator() {
        Validator.registerAnnotation(Age.class);
        Validator.registerAnnotation(PhoneThai.class);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
