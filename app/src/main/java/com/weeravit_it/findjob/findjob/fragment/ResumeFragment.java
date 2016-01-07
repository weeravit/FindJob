package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.SwipeableRecyclerViewTouchListener;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.ResumeManageActivity;
import com.weeravit_it.findjob.findjob.adapter.ResumeAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.ResumeEvent;
import com.weeravit_it.findjob.findjob.controller.ResumeController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;
import com.weeravit_it.findjob.findjob.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumeFragment extends Fragment {

    UltimateRecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    ResumeAdapter adapter;
    List<ResumeBox> resumes;

    ResumeController resumeController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        ResumeFragment fragment = new ResumeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resume, container, false);

        initInstances(view);
        configRecyclerView();

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

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void initInstances(View view) {
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycleView);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        resumes = new ArrayList<>();

        busProvider = BusProvider.getInstance(getActivity());
        resumeController = new ResumeController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        floatingActionButton.setOnClickListener(onClickListener);
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
        recyclerView.setLayoutManager(linearLayoutManager);

        SwipeableRecyclerViewTouchListener swipeableRecyclerViewTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView.mRecyclerView, swipeListener);
        recyclerView.mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
                recyclerViewOnItemClickListener));
        recyclerView.addOnItemTouchListener(swipeableRecyclerViewTouchListener);
        recyclerView.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.setDefaultOnRefreshListener(onRefreshListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ResumeManageActivity.class);
            intent.putExtra(getString(R.string.intent_key), getString(R.string.intent_add));
            startActivity(intent);
        }
    };

    ItemTouchListenerAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View clickedView, int position) {
            ResumeBox resume = resumes.get(position);
            Intent intent = new Intent(getActivity(), ResumeManageActivity.class);
            intent.putExtra(getString(R.string.intent_key), getString(R.string.intent_edit));
            intent.putExtra(getString(R.string.intent_obj), resume.getResume().getId());
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

    private SwipeableRecyclerViewTouchListener.SwipeListener swipeListener = new SwipeableRecyclerViewTouchListener.SwipeListener() {
        @Override
        public boolean canSwipe(int position) {
            return true;
        }

        @Override
        public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                ResumeBox resumeBox = resumes.get(position);
                resumeController.delete(resumeBox);
                resumes.remove(position);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
            for (int position : reverseSortedPositions) {
                ResumeBox resumeBox = resumes.get(position);
                resumeController.delete(resumeBox);
                resumes.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void setupAdapter(List<ResumeBox> result) {
        resumes.clear();
        resumes.addAll(result);

        adapter = new ResumeAdapter(resumes, getActivity(), R.layout.item_card_resume);
        recyclerView.setAdapter(adapter);

        if (resumes.size() == 20) {
            recyclerView.enableLoadmore();
            adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_loadmore, null));
        } else {
            adapter.setSpaceOfFAB();
        }
    }

    private void loadMoreAdapter(List<ResumeBox> result) {
        if (result.size() < 20) {
            recyclerView.disableLoadmore();
            adapter.setSpaceOfFAB();
        }
        resumes.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        loadData();
        recyclerView.setRefreshing(false);
    }

    private void loadMoreCont(int page) {
        resumeShow(page);
    }

    public void loadData() {
        resumeShow(1);
    }

    private void resumeShow(int page) {
        Member member = LocalStorage.getInstance().getMember();
        resumeController.showList(page, member);
    }

    @Subscribe
    public void showListSubscribe(ResumeEvent.ShowList showList) {
        if (showList.isSuccess()) {
            if (showList.getPage() == 1)
                setupAdapter(showList.getResumeBoxes());
            else
                loadMoreAdapter(showList.getResumeBoxes());
        } else {
            recyclerView.disableLoadmore();
        }
    }

    @Subscribe
    public void deleteSubscribe(ResumeEvent.Delete delete) {
        if (delete.isSuccess()) {
            Toast.makeText(getActivity(), getString(R.string.resume_delete_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.resume_manage_fail), Toast.LENGTH_SHORT).show();
        }
    }

}
