package com.weeravit_it.findjob.findjob.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weeravit_it.findjob.findjob.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateFragment extends Fragment {

    public static Fragment newInstance() {
        TemplateFragment fragment = new TemplateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template, container, false);

        initInstances(view);
        return view;
    }

    private void initInstances(View root) {

    }

}
