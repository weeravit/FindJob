package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.marshalchen.ultimaterecyclerview.ItemTouchListenerAdapter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.marshalchen.ultimaterecyclerview.uiUtils.ScrollSmoothLineaerLayoutManager;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.SearchActivity;
import com.weeravit_it.findjob.findjob.activity.SearchDetailActivity;
import com.weeravit_it.findjob.findjob.adapter.SearchDetailAdapter;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.SearchEvent;
import com.weeravit_it.findjob.findjob.controller.SearchController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.extra.SearchResult;

import java.util.ArrayList;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchDetailFragment extends Fragment {

    EditText editTextSearch;
    UltimateRecyclerView recyclerView;
    SearchDetailAdapter searchDetailAdapter;

    List<SearchResult> searchResults;

    SearchController searchController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        SearchDetailFragment fragment = new SearchDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_detail, container, false);

        initInstances(view);
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
        editTextSearch = (EditText) getActivity().findViewById(R.id.editTextSearch);
        recyclerView = (UltimateRecyclerView) root.findViewById(R.id.recyclerView);

        editTextSearch.addTextChangedListener(textWatcher);

        searchResults = new ArrayList<>();

        busProvider = BusProvider.getInstance(getActivity());
        searchController = new SearchController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());
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
            SearchResult searchResult = searchResults.get(position);
            String json = GsonManager.getInstance().getGson().toJson(searchResult);
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            intent.putExtra(getString(R.string.intent_key), getActivity().getIntent().getStringExtra(getString(R.string.intent_key)));
            intent.putExtra(getString(R.string.intent_obj), json);
            getActivity().setResult(69, intent);
            getActivity().finish();
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
                    loadMoreCont(cont, editTextSearch.getText().toString());
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

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            search(s.toString());
        }
    };

    private void checkIntent(int page, String message) {
        String intent = getActivity().getIntent().getStringExtra(getString(R.string.intent_key));

        if (intent.equals(getString(R.string.job)))
            searchController.showListJob(page, message);
        else
            searchController.showListPlace(page, message);
    }

    private void setupAdapter(List<SearchResult> result) {
        searchResults.clear();
        searchResults.addAll(result);

        searchDetailAdapter = new SearchDetailAdapter(searchResults, getActivity(), R.layout.item_card_search);
        recyclerView.setAdapter(searchDetailAdapter);

        if (searchResults.size() == 20) {
            recyclerView.enableLoadmore();
            searchDetailAdapter.setCustomLoadMoreView(LayoutInflater.from(getActivity())
                    .inflate(R.layout.custom_loadmore, null));
        }
    }

    private void loadMoreAdapter(List<SearchResult> result) {
        if (result.size() < 20)
            recyclerView.disableLoadmore();
        searchResults.addAll(result);
        searchDetailAdapter.notifyDataSetChanged();
    }

    private void pullToRefresh() {
        loadData();
        editTextSearch.setText("");
        recyclerView.setRefreshing(false);
    }

    private void search(String message) {
        checkIntent(1, message);
    }

    private void loadMoreCont(int page, String searchMsg) {
        checkIntent(page, searchMsg);
    }

    private void loadData() {
        checkIntent(1, "");
    }

    @Subscribe
    public void showListJob(SearchEvent.Job searchEvent) {
        if (searchEvent.isSuccess()) {
            if (searchEvent.getPage() == 1) {
                setupAdapter(searchEvent.getSearchResults());
            } else {
                loadMoreAdapter(searchEvent.getSearchResults());
            }
        } else {
            recyclerView.disableLoadmore();
        }
    }

    @Subscribe
    public void showListPlace(SearchEvent.Place searchEvent) {
        if (searchEvent.isSuccess()) {
            if (searchEvent.getPage() == 1) {
                setupAdapter(searchEvent.getSearchResults());
            } else {
                loadMoreAdapter(searchEvent.getSearchResults());
            }
        } else {
            recyclerView.disableLoadmore();
        }
    }

}
