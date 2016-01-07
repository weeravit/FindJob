package com.weeravit_it.findjob.findjob.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.JobDetailActivity;
import com.weeravit_it.findjob.findjob.adapter.NearAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.JobdetailEvent;
import com.weeravit_it.findjob.findjob.controller.JobdetailController;
import com.weeravit_it.findjob.findjob.controller.RecentController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Jobdetail;
import com.weeravit_it.findjob.findjob.model.Recent;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

/**
 * A simple {@link Fragment} subclass.
 */
public class NearFragment extends Fragment {

    FloatingActionButton floatingActionButton;
    AppBarLayout appBarLayout;
    UltimateRecyclerView recyclerView;
    NearAdapter adapter;
    List<Jobdetail> jobdetails;
    LatLng latLng;

    JobdetailController jobdetailController;
    RecentController recentController;
    BusProvider busProvider;

    int kilometer = 20;

    public static Fragment newInstance() {
        NearFragment fragment = new NearFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_near, container, false);

        initInstances(view);
        configRecyclerView();

        loadData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        busProvider.getBus().register(this);
        startLocation();
    }

    @Override
    public void onStop() {
        busProvider.getBus().unregister(this);
        stopLocation();
        super.onStop();
    }

    private void initInstances(View view) {
        floatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBar);
        recyclerView = (UltimateRecyclerView) view.findViewById(R.id.recycleView);
        jobdetails = new ArrayList<>();

        busProvider = BusProvider.getInstance(getActivity());
        jobdetailController = new JobdetailController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
        recentController = new RecentController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        floatingActionButton.setOnClickListener(onClickListener);
    }

    private void configRecyclerView() {
        LinearLayoutManager linearLayoutManager = new ScrollSmoothLineaerLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false, 300);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.mRecyclerView.addOnItemTouchListener(new ItemTouchListenerAdapter(recyclerView.mRecyclerView,
                recyclerViewOnItemClickListener));
        recyclerView.setOnLoadMoreListener(onLoadMoreListener);
        recyclerView.setDefaultOnRefreshListener(onRefreshListener);
    }

    private void startLocation() {
        SmartLocation.with(getActivity())
                .location()
                .start(onLocationUpdatedListener);
    }

    private void stopLocation() {
        SmartLocation.with(getActivity()).location().stop();
    }

    private ItemTouchListenerAdapter.RecyclerViewOnItemClickListener recyclerViewOnItemClickListener = new ItemTouchListenerAdapter.RecyclerViewOnItemClickListener() {
        @Override
        public void onItemClick(RecyclerView parent, View clickedView, int position) {
            Jobdetail jobdetail = jobdetails.get(position);
            Intent intent = new Intent(getActivity(), JobDetailActivity.class);
            intent.putExtra(getString(R.string.intent_key), jobdetail.getId());
            startActivity(intent);

            Recent recent = new Recent(LocalStorage.getInstance().getMember(),jobdetail);
            recentController.store(recent);
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
            appBarLayout.setExpanded(true, true);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pullToRefresh();
                }
            }, 1000);
        }
    };

    private OnLocationUpdatedListener onLocationUpdatedListener = new OnLocationUpdatedListener() {
        @Override
        public void onLocationUpdated(Location location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            loadData();
        }
    };

    private void setupAdapter(List<Jobdetail> result) {
        jobdetails.clear();
        jobdetails.addAll(result);

        adapter = new NearAdapter(jobdetails, getActivity(), R.layout.item_card_near);
        recyclerView.setAdapter(adapter);

        if (jobdetails.size() == 20) {
            recyclerView.enableLoadmore();
            adapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_loadmore, null));
        } else {
            adapter.setSpaceOfFAB();
        }
    }

    private void loadMoreAdapter(List<Jobdetail> result) {
        if (result.size() < 20) {
            recyclerView.disableLoadmore();
            adapter.setSpaceOfFAB();
        }
        jobdetails.addAll(result);
        adapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        loadData();
        recyclerView.setRefreshing(false);
    }

    private void loadMoreCont(int page) {
        nearBy(page, latLng, kilometer);
    }

    private void loadData() {
        if (latLng != null)
            nearBy(1, latLng, kilometer);
    }

    private void nearBy(int page, LatLng latLng, int kilometer) {
        jobdetailController.showListNearBy(page, latLng.latitude, latLng.longitude, kilometer);
    }

    @Subscribe
    public void showListNearBySubscribe(JobdetailEvent.ShowListNearBy showList) {
        if (showList.isSuccess()) {
            if (showList.getPage() == 1) {
                setupAdapter(showList.getJobdetails());
            } else {
                loadMoreAdapter(showList.getJobdetails());
            }
        } else {
            recyclerView.disableLoadmore();
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            final EditText editText = new EditText(getActivity());
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//            editText.setRawInputType(Configuration.KEYBOARD_12KEY);
            builder.setTitle(R.string.dialog_nearby_title);
            builder.setView(editText);
            builder.setPositiveButton(R.string.dialog_nearby_btn_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    kilometer = Integer.parseInt(editText.getText().toString());
                    loadData();
                    Toast.makeText(getActivity(), "Near by " + editText.getText().toString() + "km", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };

}
