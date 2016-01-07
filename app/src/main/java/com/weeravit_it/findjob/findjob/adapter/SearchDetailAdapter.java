package com.weeravit_it.findjob.findjob.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.model.extra.SearchResult;

import java.util.List;

/**
 * Created by Weeravit on 24/8/2558.
 */
public class SearchDetailAdapter extends UltimateViewAdapter<SearchDetailAdapter.SearchHolder> {

    private List<SearchResult> searchresults;
    private Context context;
    private int resCustomLayout;

    public SearchDetailAdapter(List<SearchResult> searchresults, Context context, int resCustomLayout) {
        this.searchresults = searchresults;
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
        return searchresults.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(SearchHolder holder, int position) {
        try {

            SearchResult searchResult = searchresults.get(position);
            holder.title.setText(searchResult.getName());

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

    public class SearchHolder extends UltimateRecyclerviewViewHolder {

        private CardView cardView;
        private TextView title;

        public SearchHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.cardView = (CardView) itemView.findViewById(R.id.cv);
                this.title = (TextView) itemView.findViewById(R.id.title);
            }
        }

    }

}
