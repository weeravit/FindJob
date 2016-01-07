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
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.ResumeManageActivity;
import com.weeravit_it.findjob.findjob.adapter.SendResumeAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.ResumeEvent;
import com.weeravit_it.findjob.findjob.bus.event.SendResumeEvent;
import com.weeravit_it.findjob.findjob.controller.ResumeController;
import com.weeravit_it.findjob.findjob.controller.SendResumeController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.model.Resume;
import com.weeravit_it.findjob.findjob.model.Sendresume;
import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendResumeFragment extends Fragment {

    UltimateRecyclerView recyclerView;
    FloatingActionButton floatingActionButton;

    SendResumeAdapter adapter;
    List<ResumeBox> resumes;
    List<ResumeBox> selectedResume;

    int jobdetail_id;

    ResumeController resumeController;
    SendResumeController sendResumeController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        SendResumeFragment fragment = new SendResumeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_resume, container, false);

        initInstances(view);
        checkIntent();
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

    private void initInstances(View root) {
        recyclerView = (UltimateRecyclerView) root.findViewById(R.id.recycleView);
        floatingActionButton = (FloatingActionButton) root.findViewById(R.id.fab);

        resumes = new ArrayList<>();
        selectedResume = new ArrayList<>();

        floatingActionButton.setOnClickListener(onClickListener);

        busProvider = BusProvider.getInstance(getActivity());
        resumeController = new ResumeController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
        sendResumeController = new SendResumeController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
    }

    private void checkIntent() {
        jobdetail_id = getActivity().getIntent().getIntExtra(getString(R.string.intent_obj), 0);
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

            ResumeBox resumeBox = resumes.get(position);
//            if (selectedResume.contains(resumeBox))
//                selectedResume.remove(resumeBox);
//            else
//                selectedResume.add(resumeBox);

            selectedResume.clear();
            selectedResume.add(resumeBox);

            adapter.notifyDataSetChanged();
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

    private void setupAdapter(List<ResumeBox> result) {
        resumes.clear();
        resumes.addAll(result);

        adapter = new SendResumeAdapter(resumes, selectedResume, getActivity(), R.layout.item_card_send_resume);
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

    private void loadData() {
        resumeShow(1);
    }

    private void resumeShow(int page) {
        Member member = LocalStorage.getInstance().getMember();
        resumeController.showList(page, member);
    }

    public void send() {
        if (resumes.size() > 0) {
            if (selectedResume.size() > 0) {
                List<Sendresume> sendresumes = new ArrayList<>();
                Iterator<ResumeBox> resumeBoxIterator = selectedResume.iterator();
                while (resumeBoxIterator.hasNext()) {
                    Resume resume = resumeBoxIterator.next().getResume();
                    Sendresume sendresume = new Sendresume(resume.getId(), jobdetail_id);
                    sendresumes.add(sendresume);
                }
                sendResumeController.send(sendresumes);
            } else {
                Toast.makeText(getActivity(), getString(R.string.sendresume_send_nonselected), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.sendresume_send_empty), Toast.LENGTH_SHORT).show();
        }
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
    public void sendSubscribe(SendResumeEvent.Send send) {
        if (send.isSuccess()) {
            Toast.makeText(getActivity(), getString(R.string.sendresume_send_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.sendresume_send_fail), Toast.LENGTH_SHORT).show();
        }
        getActivity().finish();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), ResumeManageActivity.class);
            intent.putExtra(getString(R.string.intent_key), getString(R.string.intent_add));
            startActivity(intent);
        }
    };

}
