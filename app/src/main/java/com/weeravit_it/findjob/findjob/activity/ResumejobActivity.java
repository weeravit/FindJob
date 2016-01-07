package com.weeravit_it.findjob.findjob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.adapter.ResumeJobAdapter;
import com.weeravit_it.findjob.findjob.controller.JobController;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.JobEvent;
import com.weeravit_it.findjob.findjob.fragment.ResumejobFragment;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.extra.SelectedJobs;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

public class ResumejobActivity extends AppCompatActivity {

    Toolbar toolbar;
    final int RESUMEJOB_CODE = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumejob);

        initInstances();
    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentContainner, ResumejobFragment.newInstance())
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
