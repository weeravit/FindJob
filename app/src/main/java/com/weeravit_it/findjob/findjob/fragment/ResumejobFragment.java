package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.SelectedJobsOfResumeActivity;
import com.weeravit_it.findjob.findjob.adapter.ResumeJobAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.JobEvent;
import com.weeravit_it.findjob.findjob.controller.JobController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.extra.SelectedJobs;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumejobFragment extends Fragment {

    EditText editTextSearch;
    RelativeLayout rlSelectedJobs;
    TextView tvSelectedJobs;
    UltimateRecyclerView recyclerView;
    ResumeJobAdapter resumeJobAdapter;

    List<Job> searchResults;
    List<Job> selectedJobs;

    final int RESUMEJOB_CODE = 69;

    JobController jobController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        ResumejobFragment fragment = new ResumejobFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resumejob, container, false);

        initInstances(view);
        selectedAlert();
        configRecyclerView();
        loadData();

        return view;
    }

    private void initInstances(View root) {
        editTextSearch = (EditText) getActivity().findViewById(R.id.editTextSearch);
        rlSelectedJobs = (RelativeLayout) root.findViewById(R.id.rlSelectedJobs);
        tvSelectedJobs = (TextView) root.findViewById(R.id.tvSelectedJobs);
        recyclerView = (UltimateRecyclerView) root.findViewById(R.id.recyclerView);

        editTextSearch.addTextChangedListener(textWatcher);
        rlSelectedJobs.setOnClickListener(onClickListener);

        searchResults = new ArrayList<>();
        selectedJobs = SelectedJobs.getInstance().getJobs();

        busProvider = BusProvider.getInstance(getActivity());
        jobController = new JobController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
    }

    @Override
    public void onStart() {
        super.onStart();
        busProvider.getBus().register(this);
    }

    @Override
    public void onStop() {
        busProvider.getBus().unregister(this);
        super.onStop();
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
                recyclerViewOnItemClickListener));
        recyclerView.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.setDefaultOnRefreshListener(onRefreshListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (selectedJobs.size() > 0) {
                Intent intent = new Intent(getActivity(), SelectedJobsOfResumeActivity.class);
                startActivityForResult(intent, RESUMEJOB_CODE);
            } else {
                Toast.makeText(getActivity(), getString(R.string.resumejob_text_noselect), Toast.LENGTH_SHORT).show();
            }
        }
    };

    private ItemTouchListenerAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View clickedView, int position) {

            Job jobClick = searchResults.get(position);
            if (SelectedJobs.getInstance().equal(jobClick))
                SelectedJobs.getInstance().remove(jobClick);
            else
                selectedJobs.add(searchResults.get(position));

            selectedAlert();

            resumeJobAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemLongClick(RecyclerView parent, View clickedView, int position) {

        }
    };

    private UltimateRecyclerView.OnLoadMoreListener onLoadMoreListener = new UltimateRecyclerView.OnLoadMoreListener() {
        @Override
        public void loadMore(final int i, int i1) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    int cont = (i / 20) + 1;
                    loadMoreCont(cont, editTextSearch.getText().toString());
                }
            }, 1000);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh();
                }
            }, 1000);
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            search(s.toString());
        }
    };

    private void selectedAlert() {
        String text = String.format("%s (%s)", getString(R.string.resumejob_text_selected), selectedJobs.size());
        tvSelectedJobs.setText(text);
    }

    private void checkIntent(int page, String message) {
        jobController.shows(page, message);
    }

    private void setupAdapter(List<Job> result) {
        searchResults.clear();
        searchResults.addAll(result);

        if (resumeJobAdapter == null) {
            resumeJobAdapter = new ResumeJobAdapter(searchResults, selectedJobs, getActivity(), R.layout.item_card_resumejob);
            recyclerView.setAdapter(resumeJobAdapter);
        } else {
            resumeJobAdapter.notifyDataSetChanged();
        }

        if (searchResults.size() == 20) {
            recyclerView.enableLoadmore();
            resumeJobAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_loadmore, null));
        }
    }

    private void loadMoreAdapter(List<Job> result) {
        if (result.size() < 20)
            recyclerView.disableLoadmore();
        searchResults.addAll(result);
        resumeJobAdapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        loadData();
        editTextSearch.setText("");
        recyclerView.setRefreshing(false);
    }

    private void search(String message) {
        checkIntent(1, message);
    }

    private void loadMoreCont(int page, String searchMsg) {
        checkIntent(page, searchMsg);
    }

    private void loadData() {
        checkIntent(1, "");
    }

    @Subscribe
    public void showsSubscribe(JobEvent.ShowJobs showJobs) {
        if (showJobs.isSuccess()) {
            if (showJobs.getPage() == 1) {
                setupAdapter(showJobs.getJobs());
            } else {
                loadMoreAdapter(showJobs.getJobs());
            }
        } else {
            recyclerView.disableLoadmore();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESUMEJOB_CODE == resultCode) {
            selectedAlert();
            resumeJobAdapter.notifyDataSetChanged();
        }
    }

}
