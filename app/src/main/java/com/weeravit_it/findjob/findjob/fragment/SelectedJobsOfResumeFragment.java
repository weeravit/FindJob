package com.weeravit_it.findjob.findjob.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.adapter.SelectedJobsAdapter;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.extra.SelectedJobs;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedJobsOfResumeFragment extends Fragment {

    UltimateRecyclerView recyclerView;
    SelectedJobsAdapter selectedJobsAdapter;

    List<Job> selectedJobs;

    public static Fragment newInstance() {
        SelectedJobsOfResumeFragment fragment = new SelectedJobsOfResumeFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_jobs_of_resume, container, false);

        initInstances(view);
        selectedAlert();
        configRecyclerView();

        return view;
    }

    private void initInstances(View root) {
        recyclerView = (UltimateRecyclerView) root.findViewById(R.id.recyclerView);

        selectedJobs = SelectedJobs.getInstance().getJobs();
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
        recyclerView.setLayoutManager(linearLayoutManager);

        selectedJobsAdapter = new SelectedJobsAdapter(selectedJobs, getActivity(), R.layout.item_card_resumejob);
        recyclerView.setAdapter(selectedJobsAdapter);

        recyclerView.mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
                recyclerViewOnItemClickListener));
    }

    private ItemTouchListenerAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View clickedView, int position) {

            Job jobClick = selectedJobs.get(position);
            SelectedJobs.getInstance().remove(jobClick);

            selectedAlert();

            selectedJobsAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemLongClick(RecyclerView parent, View clickedView, int position) {

        }
    };

    private void selectedAlert() {
        String text = String.format("%s (%s)", getString(R.string.resumejob_text_selected), selectedJobs.size());
        getActivity().setTitle(text);
    }

}
