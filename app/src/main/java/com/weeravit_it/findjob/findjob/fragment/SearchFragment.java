package com.weeravit_it.findjob.findjob.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.SearchDetailActivity;
import com.weeravit_it.findjob.findjob.activity.SearchResultActivity;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Categoryjob;
import com.weeravit_it.findjob.findjob.model.District;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.Province;
import com.weeravit_it.findjob.findjob.model.extra.SearchJob;
import com.weeravit_it.findjob.findjob.model.extra.SearchResult;

import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    EditText editTextNotice;
    EditText editTextJobAndJobCategory;
    EditText editTextDistrictAndProvince;
    MaterialSpinner spinnerSalary;
    Button btnOK;

    public static Fragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        initInstances(view);
        setupSpinner();

        return view;
    }

    private void initInstances(View root) {
        editTextNotice = (EditText) root.findViewById(R.id.editTextNotice);
        editTextJobAndJobCategory = (EditText) root.findViewById(R.id.editTextJobAndJobCategory);
        editTextDistrictAndProvince = (EditText) root.findViewById(R.id.editTextDistrictAndProvince);
        spinnerSalary = (MaterialSpinner) root.findViewById(R.id.spinnerSalary);
        btnOK = (Button) root.findViewById(R.id.btnOK);

        SearchJob.getInstance().clear();

        editTextJobAndJobCategory.setOnClickListener(onClickListener);
        editTextDistrictAndProvince.setOnClickListener(onClickListener);
        btnOK.setOnClickListener(onClickListener);
    }

    private void setupSpinner() {
        String[] ITEMS = getResources().getStringArray(R.array.salary);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSalary.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == resultCode)
            changeValueInEditText(data);
    }

    private void changeValueInEditText(Intent data) {
        String intent = data.getStringExtra(getString(R.string.intent_key));
        String json = data.getStringExtra(getString(R.string.intent_obj));
        SearchResult searchResult = GsonManager.getInstance().getGson().fromJson(json, SearchResult.class);
        SearchJob searchJob = SearchJob.getInstance();

        if (intent.equals(getString(R.string.job))) {
            editTextJobAndJobCategory.setText(searchResult.getName());
            if (searchResult.getTag().equals(getString(R.string.tag_job))) {
                searchJob.setJob(new Job(searchResult.getId()));
                searchJob.setCategoryjob(null);
            } else {
                searchJob.setCategoryjob(new Categoryjob(searchResult.getId()));
                searchJob.setJob(null);
            }
        } else {
            editTextDistrictAndProvince.setText(searchResult.getName());
            if (searchResult.getTag().equals(getString(R.string.tag_district))) {
                searchJob.setDistrict(new District(searchResult.getId()));
                searchJob.setProvince(null);
            } else {
                searchJob.setProvince(new Province(searchResult.getId()));
                searchJob.setDistrict(null);
            }
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), SearchDetailActivity.class);

            if (v == editTextJobAndJobCategory)
                intent.putExtra(getString(R.string.intent_key), getString(R.string.job));
            else if (v == editTextDistrictAndProvince)
                intent.putExtra(getString(R.string.intent_key), getString(R.string.place));
            else {
                String salary = null;
                if(!spinnerSalary.getSelectedItem().toString().equals(getString(R.string.salary)))
                    salary = spinnerSalary.getSelectedItem().toString();
                SearchJob.getInstance().setNotice(editTextNotice.getText().toString());
                SearchJob.getInstance().setSalary(salary);
                Intent i = new Intent(getActivity(), SearchResultActivity.class);
                startActivity(i);
                return;
            }

            startActivityForResult(intent, 69);
        }
    };

}
