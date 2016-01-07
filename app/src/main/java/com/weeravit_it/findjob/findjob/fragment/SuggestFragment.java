package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.JobDetailActivity;
import com.weeravit_it.findjob.findjob.adapter.SuggestAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.SuggestEvent;
import com.weeravit_it.findjob.findjob.controller.RecentController;
import com.weeravit_it.findjob.findjob.controller.SuggestController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Jobsuggest;
import com.weeravit_it.findjob.findjob.model.Recent;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuggestFragment extends Fragment {

    AppBarLayout appBarLayout;
    UltimateRecyclerView recyclerView;
    SuggestAdapter adapter;
    List<Jobsuggest> jobsuggests;

    SuggestController suggestController;
    RecentController recentController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        SuggestFragment fragment = new SuggestFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggest, container, false);

        initInstances(view);
        configRecyclerView();

        loadData();
        return view;
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

    private void initInstances(View view) {
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBar);
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycleView);
        jobsuggests = new ArrayList<>();

        busProvider = BusProvider.getInstance(getActivity());
        suggestController = new SuggestController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
        recentController = new RecentController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
                recyclerViewOnItemClickListener));
        recyclerView.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.setDefaultOnRefreshListener(onRefreshListener);
    }

    private ItemTouchListenerAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View clickedView, int position) {
            Jobdetail jobdetail = jobsuggests.get(position).getJobdetail();
            Intent intent = new Intent(getActivity(), JobDetailActivity.class);
            intent.putExtra(getString(R.string.intent_key), jobdetail.getId());
            startActivity(intent);

            Recent recent = new Recent(LocalStorage.getInstance().getMember(), jobdetail);
            recentController.store(recent);
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
                    loadMoreCont(cont);
                }
            }, 1000);
        }
    };

    private SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            appBarLayout.setExpanded(true, true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh();
                }
            }, 1000);
        }
    };

    private void setupAdapter(List<Jobsuggest> result) {
        jobsuggests.clear();
        jobsuggests.addAll(result);

        adapter = new SuggestAdapter(jobsuggests, getActivity(), R.layout.item_card_jobdetail);
        recyclerView.setAdapter(adapter);

        if (jobsuggests.size() == 20) {
            recyclerView.enableLoadmore();
            adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_loadmore, null));
        }
    }

    private void loadMoreAdapter(List<Jobsuggest> result) {
        if (result.size() < 20)
            recyclerView.disableLoadmore();
        jobsuggests.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        loadData();
        recyclerView.setRefreshing(false);
    }

    private void loadMoreCont(int page) {
        getFeed(page);
    }

    private void loadData() {
        getFeed(1);
    }

    private void getFeed(int page) {
        suggestController.showList(page);
    }

    @Subscribe
    public void showListSubscribe(SuggestEvent.ShowList showList) {
        if (showList.isSuccess()) {
            if (showList.getPage() == 1) {
                setupAdapter(showList.getJobsuggests());
            } else {
                loadMoreAdapter(showList.getJobsuggests());
            }
        } else {
            recyclerView.disableLoadmore();
        }
    }

}
