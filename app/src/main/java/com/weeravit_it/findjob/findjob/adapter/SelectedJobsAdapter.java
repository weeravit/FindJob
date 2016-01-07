package com.weeravit_it.findjob.findjob.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.model.Job;

import java.util.List;

/**
 * Created by Weeravit on 24/8/2558.
 */
public class SelectedJobsAdapter extends UltimateViewAdapter<SelectedJobsAdapter.SearchHolder> {

    private List<Job> selectedJobs;
    private Context context;
    private int resCustomLayout;

    public SelectedJobsAdapter(List<Job> selectedJobs, Context context, int resCustomLayout) {
        this.selectedJobs = selectedJobs;
        this.context = context;
        this.resCustomLayout = resCustomLayout;
    }

    // ViewHolder of LoadMore
    @Override
    public SearchHolder getViewHolder(View view) {
        return new SearchHolder(view, false);
    }

    @Override
    public SearchHolder onCreateViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resCustomLayout, null);
        SearchHolder searchHolder = new SearchHolder(view, true);
        return searchHolder;
    }

    @Override
    public int getAdapterItemCount() {
        return selectedJobs.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        try {

            Job searchResult = selectedJobs.get(position);
            holder.title.setText(searchResult.getName());
            holder.checkBox.setChecked(true); // Default

        } catch (Exception e) {
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        }
    };

    public class SearchHolder extends UltimateRecyclerviewViewHolder {

        private CardView cardView;
        private TextView title;
        private CheckBox checkBox;

        public SearchHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.cardView = (CardView) itemView.findViewById(R.id.cv);
                this.title = (TextView) itemView.findViewById(R.id.title);
                this.checkBox = (CheckBox) itemView.findViewById(R.id.cbJob);
            }
        }

    }

}
