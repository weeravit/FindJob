package com.weeravit_it.findjob.findjob.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.adapter.SelectedJobsAdapter;
import com.weeravit_it.findjob.findjob.fragment.SelectedJobsOfResumeFragment;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.extra.SelectedJobs;

import java.util.List;

public class SelectedJobsOfResumeActivity extends AppCompatActivity {

    Toolbar toolbar;
    final int RESUMEJOB_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_jobs_of_resume);

        initInstances();
    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainner, SelectedJobsOfResumeFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESUMEJOB_CODE, null);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESUMEJOB_CODE, null);
        finish();
        super.onBackPressed();
    }

}
