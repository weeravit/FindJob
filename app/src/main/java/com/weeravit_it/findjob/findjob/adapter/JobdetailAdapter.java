package com.weeravit_it.findjob.findjob.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.weeravit_it.findjob.findjob.utils.ColorSequence;

import java.util.List;

/**
 * Created by Weeravit on 24/8/2558.
 */
public class JobdetailAdapter extends UltimateViewAdapter<JobdetailAdapter.MyHolder> {

    private List<Jobdetail> jobdetails;
    private Context context;
    private int resCustomLayout;

    public JobdetailAdapter(List<Jobdetail> jobdetails, Context context, int resCustomLayout) {
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(resCustomLayout, null);
        MyHolder searchHolder = new MyHolder(view, true);
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
            Jobdetail jobdetail = jobdetails.get(position);

            String address = String.format("%s %s, %s", context.getString(R.string.font_address), jobdetail.getOperator().getDistrict().getName(), jobdetail.getOperator().getDistrict().getProvince().getName());
            String company = String.format("%s %s", context.getString(R.string.font_company), jobdetail.getOperator().getName());
            String salary = String.format("%s %s", context.getString(R.string.font_salary), jobdetail.getSalary());
            String datetime = String.format("%s %s", context.getString(R.string.font_clock), jobdetail.getCreatedAt());

            Glide.with(context)
                    .load(jobdetail.getOperator().getImageUrl())
                    .crossFade()
                    .into(holder.thumbnail);
            holder.notice.setText(jobdetail.getNotice());
            holder.address.setText(address);
            holder.company.setText(company);
            holder.salary.setText(salary);
            holder.datetime.setText(datetime);
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

    public class MyHolder extends UltimateRecyclerviewViewHolder {

        protected CardView cardView;
        protected ImageView thumbnail;
        protected TextView notice;
        protected IconTextView address;
        protected IconTextView company;
        protected IconTextView salary;
        protected IconTextView datetime;

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
            }
        }

    }

}
