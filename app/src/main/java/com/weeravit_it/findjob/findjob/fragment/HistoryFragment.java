package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.weeravit_it.findjob.findjob.adapter.JobdetailAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.RecentEvent;
import com.weeravit_it.findjob.findjob.controller.RecentController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private UltimateRecyclerView recyclerView;
    private JobdetailAdapter adapter;
    private List<Jobdetail> jobdetails;

    private RecentController recentController;
    private BusProvider busProvider;

    public static Fragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

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
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycleView);
        jobdetails = new ArrayList<>();

        busProvider = BusProvider.getInstance(getActivity());
        recentController = new RecentController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
                recyclerViewOnItemClickListener));
        recyclerView.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.setDefaultOnRefreshListener(onRefreshListener);
    }

    private ItemTouchListenerAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View clickedView, int position) {
            Jobdetail jobdetail = jobdetails.get(position);
            Intent intent = new Intent(getActivity(), JobDetailActivity.class);
            intent.putExtra(getString(R.string.intent_key), jobdetail.getId());
            startActivity(intent);
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
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh();
                }
            }, 1000);
        }
    };

    private void setupAdapter(List<Jobdetail> result) {
        jobdetails.clear();
        jobdetails.addAll(result);

        adapter = new JobdetailAdapter(jobdetails, getActivity(), R.layout.item_card_jobdetail);
        recyclerView.setAdapter(adapter);

        if (jobdetails.size() == 20) {
            recyclerView.enableLoadmore();
            adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_loadmore, null));
        }
    }

    private void loadMoreAdapter(List<Jobdetail> result) {
        if (result.size() < 20)
            recyclerView.disableLoadmore();
        jobdetails.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        loadData();
        recyclerView.setRefreshing(false);
    }

    private void loadMoreCont(int page) {
        recentShow(page);
    }

    private void loadData() {
        recentShow(1);
    }

    private void recentShow(int page) {
        Member member = LocalStorage.getInstance().getMember();
        recentController.showList(page, member);
    }

    @Subscribe
    public void showListSubscribe(RecentEvent.ShowList showList) {
        if (showList.isSuccess()) {
            if (showList.getPage() == 1)
                setupAdapter(showList.getJobdetails());
            else
                loadMoreAdapter(showList.getJobdetails());
        } else {
            recyclerView.disableLoadmore();
        }
    }

}
