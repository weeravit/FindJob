package com.weeravit_it.findjob.findjob.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initInstances();
    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setVisibility(View.GONE); // Hide Toolbar

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainner, LoginFragment.newInstance())
                .commit();
    }

}
