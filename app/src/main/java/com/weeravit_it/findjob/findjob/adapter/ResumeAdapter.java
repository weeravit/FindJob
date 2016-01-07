package com.weeravit_it.findjob.findjob.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.model.Resume;
import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;
import com.weeravit_it.findjob.findjob.utils.Contextor;

import java.util.List;

/**
 * Created by Weeravit on 24/8/2558.
 */
public class ResumeAdapter extends UltimateViewAdapter<ResumeAdapter.MyHolder> {

    private List<ResumeBox> resumes;
    private Context context;
    private int resCustomLayout;

    private int position_item = 0;

    public ResumeAdapter(List<ResumeBox> resumes, Context context, int resCustomLayout) {
        this.resumes = resumes;
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
        MyHolder myHolder;
        if (resumes.get(position_item) != null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(resCustomLayout, null);
            myHolder = new MyHolder(view, true);
        } else {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fab, null);
            myHolder = new MyHolder(view, false);
        }
        return myHolder;
    }

    @Override
    public int getAdapterItemCount() {
        return resumes.size();
    }

    @Override
    public long generateHeaderId(int i) {
        return 0;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        try {
            position_item = position + 1;
            if (resumes.get(position) != null) {
                ResumeBox resumeBox = resumes.get(position);
                Resume resume = resumeBox.getResume();

                String resumeNo = String.format("%s%s", Contextor.getInstance().getMainContext().getString(R.string.resume_no), position+1);

                Glide.with(context)
                        .load(resumeBox.getImage())
                        .crossFade()
                        .into(holder.ivResumeImage);
                holder.tvResumeNo.setText(resumeNo);
                holder.tvResumeDetail.setText(resume.getObjective());
            } else {
                position_item = 0;
            }

        } catch (Exception e) {
        }
    }

    public void setSpaceOfFAB() {
        resumes.add(null);
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

        protected ImageView ivResumeImage;
        protected TextView tvResumeNo;
        protected TextView tvResumeDetail;

        public MyHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                this.ivResumeImage = (ImageView) itemView.findViewById(R.id.ivResumeImage);
                this.tvResumeNo = (TextView) itemView.findViewById(R.id.tvResumeNo);
                this.tvResumeDetail = (TextView) itemView.findViewById(R.id.tvResumeDetail);
            }
        }

    }

}
