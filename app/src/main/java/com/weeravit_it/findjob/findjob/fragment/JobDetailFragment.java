package com.weeravit_it.findjob.findjob.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.joanzapata.iconify.widget.IconTextView;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.SendResumeActivity;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.FavoriteEvent;
import com.weeravit_it.findjob.findjob.bus.event.JobdetailEvent;
import com.weeravit_it.findjob.findjob.controller.FavoriteController;
import com.weeravit_it.findjob.findjob.controller.JobdetailController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class JobDetailFragment extends Fragment {

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ImageView ivLogo;
    private LinearLayout linearLayoutTabOptions;
    private IconTextView iconFavorite;
    private IconTextView iconResume;
    private IconTextView iconNavigator;
    private LinearLayout layoutNavigator;
    private TextView tvNotice;
    private TextView tvJob;
    private TextView tvDescription;
    private TextView tvRequirement;
    private TextView tvWelfare;
    private TextView tvSalary;
    private TextView tvAddress;
    private ProgressDialog progressDialog;

    private Jobdetail jobdetail;
    private boolean isFavorite;

    private JobdetailController jobdetailController;
    private FavoriteController favoriteController;
    private BusProvider busProvider;

    public static Fragment newInstance() {
        JobDetailFragment fragment = new JobDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_job_detail, container, false);

        initInstances(view);
        checkIntent();

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
        collapsingToolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ivLogo = (ImageView) getActivity().findViewById(R.id.ivLogo);

        linearLayoutTabOptions = (LinearLayout) root.findViewById(R.id.linearLayoutTabOptions);
        iconFavorite = (IconTextView) root.findViewById(R.id.iconFavorite);
        iconResume = (IconTextView) root.findViewById(R.id.iconResume);
        iconNavigator = (IconTextView) root.findViewById(R.id.iconNavigator);
        layoutNavigator = (LinearLayout) root.findViewById(R.id.layoutNavigator);
        tvAddress = (TextView) root.findViewById(R.id.tvAddress);
        tvSalary = (TextView) root.findViewById(R.id.tvSalary);
        tvWelfare = (TextView) root.findViewById(R.id.tvWelfare);
        tvRequirement = (TextView) root.findViewById(R.id.tvRequirement);
        tvDescription = (TextView) root.findViewById(R.id.tvDescription);
        tvJob = (TextView) root.findViewById(R.id.tvJob);
        tvNotice = (TextView) root.findViewById(R.id.tvNotice);

//        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(R.color.black));

        iconFavorite.setOnClickListener(onClickListener);
        iconResume.setOnClickListener(onClickListener);
        iconNavigator.setOnClickListener(onClickListener);

        isFavorite = true;

        busProvider = BusProvider.getInstance(getActivity());
        jobdetailController = new JobdetailController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
        favoriteController = new FavoriteController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        authorizeMember();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void authorizeMember() {
        Member member = LocalStorage.getInstance().getMember();
        if (member == null)
            linearLayoutTabOptions.setVisibility(View.GONE);
    }

    private void checkIntent() {
        Intent intent = getActivity().getIntent();
        int jobdetailId = intent.getIntExtra(getString(R.string.intent_key), -1);
        if (jobdetailId != -1) {
            jobdetailController.show(jobdetailId);
        }
    }

    private void setupTextView() {
//        collapsingToolbarLayout.setTitle(jobdetail.getOperator().getName());
        toolbar.setTitle(jobdetail.getOperator().getName());
        Glide.with(getActivity())
                .load(jobdetail.getOperator().getImageUrl())
                .crossFade()
                .centerCrop()
                .into(ivLogo);
        tvNotice.setText(jobdetail.getNotice());
        tvJob.setText(jobdetail.getJob().getName());
        tvDescription.setText(Html.fromHtml(jobdetail.getDescription()));
        tvRequirement.setText(Html.fromHtml(jobdetail.getRequirement()));
        tvWelfare.setText(Html.fromHtml(jobdetail.getWelfare()));
        tvAddress.setText(jobdetail.getOperator().getAddress());
        tvSalary.setText(jobdetail.getSalary());
    }

    private void navigation(LatLng latLng) {
        String location = String.format("%s,%s", latLng.latitude, latLng.longitude);
        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        try {
            startActivity(mapIntent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), getString(R.string.navigator_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void showJobdetailSubscribe(JobdetailEvent.Show showJobdetailEvent) {
        if (showJobdetailEvent.isSuccess()) {
            this.jobdetail = showJobdetailEvent.getJobdetail();
            setupTextView();

            // Call to check this favorite
            if (LocalStorage.getInstance().getMember() != null)
                favoriteController.check(jobdetail.getId(), LocalStorage.getInstance().getMember().getId());
            else
                progressDialog.dismiss();
        } else {
            Toast.makeText(getActivity(), getString(R.string.status_fail), Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe
    public void checkFavoriteSubscribe(FavoriteEvent.Check checkFavoriteEvent) {
        if (checkFavoriteEvent.isSuccess()) {
            isFavorite = checkFavoriteEvent.isChecked();
            iconFavorite.setTextColor(getResources().getColor(R.color.pink_500));
        } else {
            isFavorite = checkFavoriteEvent.isChecked();
            iconFavorite.setTextColor(getResources().getColor(R.color.gray));
        }

        progressDialog.dismiss();
    }

    @Subscribe
    public void storeFavoriteSubscribe(FavoriteEvent.Store store) {
        if (store.isSuccess()) {

        } else {

        }
        progressDialog.dismiss();
    }

    @Subscribe
    public void deleteFavoriteSubscribe(FavoriteEvent.Delete deleteFavoriteEvent) {
        if (deleteFavoriteEvent.isSuccess()) {
            isFavorite = false;
            iconFavorite.setTextColor(getResources().getColor(R.color.gray));
        } else {
            isFavorite = true;
            iconFavorite.setTextColor(getResources().getColor(R.color.pink_500));
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == iconFavorite) {
                if (isFavorite) {
                    isFavorite = false;
                    iconFavorite.setTextColor(getResources().getColor(R.color.gray));
                    favoriteController.delete(LocalStorage.getInstance().getMember(), jobdetail);
                } else {
                    isFavorite = true;
                    iconFavorite.setTextColor(getResources().getColor(R.color.pink_500));
                    favoriteController.store(LocalStorage.getInstance().getMember(), jobdetail);
                    progressDialog.show();
                }
            } else if (v == iconResume) {
                Intent intent = new Intent(getActivity(), SendResumeActivity.class);
                intent.putExtra(getString(R.string.intent_obj), jobdetail.getId());
                startActivity(intent);
            } else {
                // Operator has not lat&lng
                if (jobdetail.getOperator().getLat().isEmpty() || jobdetail.getOperator().getLng().isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.error_gps_null), Toast.LENGTH_SHORT).show();
                } else {
                    double lat = Double.parseDouble(jobdetail.getOperator().getLat());
                    double lng = Double.parseDouble(jobdetail.getOperator().getLng());
                    LatLng latLng = new LatLng(lat, lng);
                    navigation(latLng);
                }
            }
        }
    };

}
