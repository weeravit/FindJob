package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.AccountManageActivity;
import com.weeravit_it.findjob.findjob.activity.LoginActivity;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.MemberEvent;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;
import com.weeravit_it.findjob.findjob.utils.Logger;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private TextView tvFullname;
    private LinearLayout layoutProfile;
    private LinearLayout layoutFavorite;
    private LinearLayout layoutHistory;
    private LinearLayout layoutResume;
    private LinearLayout layoutLogout;

    BusProvider busProvider;

    public static Fragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        initInstances(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setupName();
    }

    private void initInstances(View root) {
        tvFullname = (TextView) root.findViewById(R.id.tvFullname);
        layoutLogout = (LinearLayout) root.findViewById(R.id.layoutLogout);
        layoutResume = (LinearLayout) root.findViewById(R.id.layoutResume);
        layoutHistory = (LinearLayout) root.findViewById(R.id.layoutHistory);
        layoutFavorite = (LinearLayout) root.findViewById(R.id.layoutFavorite);
        layoutProfile = (LinearLayout) root.findViewById(R.id.layoutProfile);

        layoutProfile.setOnClickListener(onClickListener);
        layoutFavorite.setOnClickListener(onClickListener);
        layoutHistory.setOnClickListener(onClickListener);
        layoutResume.setOnClickListener(onClickListener);
        layoutLogout.setOnClickListener(onClickListener);

        busProvider = BusProvider.getInstance(getActivity());
    }

    private void setupName() {
        Member member = LocalStorage.getInstance().getMember();
        String firstname = (member.getFirstname() == null) ? "" : member.getFirstname();
        String lastname = (member.getLastname() == null) ? "" : member.getLastname();
        String fullname = String.format("%s %s", firstname, lastname);
        tvFullname.setText(fullname);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == layoutProfile) {
                profile();
            } else if (v == layoutFavorite) {
                favorite();
            } else if (v == layoutHistory) {
                history();
            } else if (v == layoutResume) {
                resume();
            } else {
                logout();
            }
        }
    };

    private void profile() {
        Intent intent = new Intent(getActivity(), AccountManageActivity.class);
        intent.putExtra(getString(R.string.intent_key), getString(R.string.title_fragment_profile));
        startActivity(intent);
    }

    private void favorite() {
        Intent intent = new Intent(getActivity(), AccountManageActivity.class);
        intent.putExtra(getString(R.string.intent_key), getString(R.string.title_fragment_favorite));
        startActivity(intent);
    }

    private void history() {
        Intent intent = new Intent(getActivity(), AccountManageActivity.class);
        intent.putExtra(getString(R.string.intent_key), getString(R.string.title_fragment_history));
        startActivity(intent);
    }
    private void resume() {
        Intent intent = new Intent(getActivity(), AccountManageActivity.class);
        intent.putExtra(getString(R.string.intent_key), getString(R.string.title_fragment_resume));
        startActivity(intent);
    }

    private void logout() {
        LocalStorage.getInstance().clear();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

}
