package com.weeravit_it.findjob.findjob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.fragment.FavoriteFragment;
import com.weeravit_it.findjob.findjob.fragment.HistoryFragment;
import com.weeravit_it.findjob.findjob.fragment.ProfileFragment;
import com.weeravit_it.findjob.findjob.fragment.ResumeFragment;

public class AccountManageActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manage);

        initInstances();
        checkIntent();
    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void checkIntent() {
        Intent intent = getIntent();
        String manage = intent.getStringExtra(getString(R.string.intent_key));
        setTitle(manage);

        Fragment fragment = null;
        if(manage.equals(getString(R.string.title_fragment_profile)))
            fragment = ProfileFragment.newInstance();
        else if(manage.equals(getString(R.string.title_fragment_favorite)))
            fragment = FavoriteFragment.newInstance();
        else if(manage.equals(getString(R.string.title_fragment_history)))
            fragment = HistoryFragment.newInstance();
        else
            fragment = ResumeFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contentContainner, fragment)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
