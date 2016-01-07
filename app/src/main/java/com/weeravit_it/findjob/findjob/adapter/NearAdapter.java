package com.weeravit_it.findjob.findjob.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.joanzapata.iconify.widget.IconTextView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.utils.Logger;

import java.util.List;

/**
 * Created by Weeravit on 24/8/2558.
 */
public class NearAdapter extends UltimateViewAdapter<NearAdapter.MyHolder> {

    private List<Jobdetail> jobdetails;
    private Context context;
    private int resCustomLayout;

    private int position_item = 0;

    public NearAdapter(List<Jobdetail> jobdetails, Context context, int resCustomLayout) {
        this.jobdetails = jobdetails;
        this.context = context;
        this.resCustomLayout = resCustomLayout;
    }

    // ViewHolder of LoadMore
    @Override
    public MyHolder getViewHolder(View view) {
        return new MyHolder(view, false);
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup viewGroup) {
        View view;
        MyHolder searchHolder;
        if (jobdetails.get(position_item) != null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(resCustomLayout, null);
            searchHolder = new MyHolder(view, true);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fab, null);
            searchHolder = new MyHolder(view, false);
        }
        return searchHolder;
    }

    @Override
    public int getAdapterItemCount() {
        return jobdetails.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        try {
            position_item = position + 1;
            if (jobdetails.get(position) != null) {
                Jobdetail jobdetail = jobdetails.get(position);

                String address = String.format("%s %s, %s", context.getString(R.string.font_address), jobdetail.getOperator().getDistrict().getName(), jobdetail.getOperator().getDistrict().getProvince().getName());
                String company = String.format("%s %s", context.getString(R.string.font_company), jobdetail.getOperator().getName());
                String salary = String.format("%s %s", context.getString(R.string.font_salary), jobdetail.getSalary());
                String datetime = String.format("%s %s", context.getString(R.string.font_clock), jobdetail.getCreatedAt());
                String distance = String.format("%s%s", jobdetail.getDistance(), context.getString(R.string.distance_km));

                Glide.with(context)
                        .load(jobdetail.getOperator().getImageUrl())
                        .crossFade()
                        .into(holder.thumbnail);
                holder.notice.setText(jobdetail.getNotice());
                holder.address.setText(address);
                holder.company.setText(company);
                holder.salary.setText(salary);
                holder.datetime.setText(datetime);
                holder.distance.setText(distance);
            } else {
                position_item = 0;
            }

        } catch (Exception e) {
        }
    }

    public void setSpaceOfFAB() {
        jobdetails.add(null);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {

    }

    public class MyHolder extends UltimateRecyclerviewViewHolder {

        private CardView cardView;
        private ImageView thumbnail;
        private TextView notice;
        private IconTextView address;
        private IconTextView company;
        private IconTextView salary;
        private IconTextView datetime;
        private TextView distance;

        public MyHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.cardView = (CardView) itemView.findViewById(R.id.cv);
                this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
                this.notice = (TextView) itemView.findViewById(R.id.notice);
                this.address = (IconTextView) itemView.findViewById(R.id.address);
                this.company = (IconTextView) itemView.findViewById(R.id.company);
                this.salary = (IconTextView) itemView.findViewById(R.id.salary);
                this.datetime = (IconTextView) itemView.findViewById(R.id.datetime);
                this.distance = (TextView) itemView.findViewById(R.id.tvDistance);
            }
        }

    }

}
