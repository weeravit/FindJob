package com.weeravit_it.findjob.findjob.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.MemberEvent;
import com.weeravit_it.findjob.findjob.controller.MemberController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;
import com.weeravit_it.findjob.findjob.utils.RegexPattern;
import com.weeravit_it.findjob.findjob.utils.validates.Age;
import com.weeravit_it.findjob.findjob.utils.validates.PhoneThai;

import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @NotEmpty(messageResId = R.string.validation_empty)
    @Email(messageResId = R.string.validation_email)
    EditText editTextEmail;

    @NotEmpty(messageResId = R.string.validation_empty)
    EditText editTextPassword;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Length(min = 9, max = 10, messageResId = R.string.validation_length_10)
    @PhoneThai(messageResId = R.string.validation_phone)
    EditText editTextTelephoneNumber;

    @Age(messageResId = R.string.validation_age)
    @Pattern(regex = RegexPattern.NUMBER, messageResId = R.string.validation_number)
    EditText editTextAge;

    EditText editTextFirstname;
    EditText editTextLastname;
    EditText editTextAddress;
    Button btnCancel;
    Button btnOK;
    LinearLayout linearLayoutBtn;

    Validator validator;
    MemberController memberController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initInstances(view);
        modeShow();

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

    private void initInstances(View view) {
        setHasOptionsMenu(true);

        linearLayoutBtn = (LinearLayout) view.findViewById(R.id.linearLayoutBtn);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOK = (Button) view.findViewById(R.id.btnOK);
        editTextAddress = (EditText) view.findViewById(R.id.editTextAddress);
        editTextTelephoneNumber = (EditText) view.findViewById(R.id.editTextTelephoneNumber);
        editTextAge = (EditText) view.findViewById(R.id.editTextAge);
        editTextLastname = (EditText) view.findViewById(R.id.editTextLastname);
        editTextFirstname = (EditText) view.findViewById(R.id.editTextFirstname);
        editTextPassword = (EditText) view.findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) view.findViewById(R.id.editTextEmail);

        validator = new Validator(this);
        validator.setValidationListener(validationListener);

        busProvider = BusProvider.getInstance(getActivity());
        memberController = new MemberController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        btnCancel.setOnClickListener(onClickListener);
        btnOK.setOnClickListener(onClickListener);
    }

    private void modeShow() {
        linearLayoutBtn.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);

        Member member = LocalStorage.getInstance().getMember();
        editTextAddress.setText(member.getAddress());
        editTextTelephoneNumber.setText(member.getTel());
        editTextAge.setText(String.valueOf(member.getAge()));
        editTextLastname.setText(member.getLastname());
        editTextFirstname.setText(member.getFirstname());
        editTextPassword.setText("********");
        editTextEmail.setText(member.getEmail());

        String notValue = getString(R.string.profile_value_no);
        if (member.getAddress() == null) {
            editTextAddress.setText(notValue);
            editTextAddress.setTextColor(getResources().getColor(R.color.red_500));
        }
        if (member.getAge() == 0) {
            editTextAge.setText(String.valueOf(notValue));
            editTextAge.setTextColor(getResources().getColor(R.color.red_500));
        }
        if (member.getLastname() == null) {
            editTextLastname.setText(notValue);
            editTextLastname.setTextColor(getResources().getColor(R.color.red_500));
        }
        if (member.getFirstname() == null) {
            editTextFirstname.setText(notValue);
            editTextFirstname.setTextColor(getResources().getColor(R.color.red_500));
        }

        editTextAddress.setEnabled(false);
        editTextTelephoneNumber.setEnabled(false);
        editTextAge.setEnabled(false);
        editTextLastname.setEnabled(false);
        editTextFirstname.setEnabled(false);
        editTextPassword.setEnabled(false);
//        editTextEmail.setEnabled(false);
    }

    private void modeEdit() {
        linearLayoutBtn.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);

        editTextAddress.setEnabled(true);
        editTextTelephoneNumber.setEnabled(true);
        editTextAge.setEnabled(true);
        editTextLastname.setEnabled(true);
        editTextFirstname.setEnabled(true);
        editTextPassword.setEnabled(true);
//        editTextEmail.setEnabled(true);

        Member member = LocalStorage.getInstance().getMember();
        if (member.getAddress() == null) {
            editTextAddress.setText("");
            editTextAddress.setTextColor(getResources().getColor(R.color.gray_dark));
        }
        if (member.getAge() == 0) {
            editTextAge.setText(String.valueOf(""));
            editTextAge.setTextColor(getResources().getColor(R.color.gray_dark));
        }
        if (member.getLastname() == null) {
            editTextLastname.setText("");
            editTextLastname.setTextColor(getResources().getColor(R.color.gray_dark));
        }
        if (member.getFirstname() == null) {
            editTextFirstname.setText("");
            editTextFirstname.setTextColor(getResources().getColor(R.color.gray_dark));
        }
    }

    private void update() {
        Member member = new Member(
                editTextFirstname.getText().toString(),
                editTextLastname.getText().toString(),
                editTextAddress.getText().toString(),
                Integer.parseInt(editTextAge.getText().toString()),
                editTextTelephoneNumber.getText().toString(),
                editTextEmail.getText().toString(),
                editTextPassword.getText().toString()
        );
        memberController.update(member);
    }

    @Subscribe
    public void updateSubscribe(MemberEvent.Update update) {
        if (update.isSuccess()) {
            Toast.makeText(getActivity(), getString(R.string.profile_update_success), Toast.LENGTH_SHORT).show();
            modeShow();
        } else {
            Toast.makeText(getActivity(), getString(R.string.profile_update_fail), Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnCancel) {
                modeShow();
            } else {
                validator.validate();
            }
        }
    };

    Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override
        public void onValidationSucceeded() {
            update();
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            String msgError = "";
            for (ValidationError error : errors) {
                if (error.getView().equals(editTextEmail)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Email: " + rule.getMessage(getActivity()) + "\n";
                    }
                } else if (error.getView().equals(editTextPassword)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Password: " + rule.getMessage(getActivity()) + "\n";
                    }
                } else if (error.getView().equals(editTextAge)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Age: " + rule.getMessage(getActivity()) + "\n";
                    }
                } else {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Tel No: " + rule.getMessage(getActivity()) + "\n";
                    }
                }
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(R.string.dialog_validation_title);
            builder.setMessage(msgError);
            builder.setPositiveButton(R.string.dialog_validation_btn_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            modeEdit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
