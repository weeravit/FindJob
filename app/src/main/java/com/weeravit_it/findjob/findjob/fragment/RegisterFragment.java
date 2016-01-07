package com.weeravit_it.findjob.findjob.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.MainActivity;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.MemberEvent;
import com.weeravit_it.findjob.findjob.controller.MemberController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.RegexPattern;
import com.weeravit_it.findjob.findjob.utils.validates.PhoneThai;

import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    @NotEmpty(messageResId = R.string.validation_empty)
    @Email(messageResId = R.string.validation_email)
    EditText editTextEmail;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Password(min = 4, messageResId = R.string.validation_password)
    EditText editTextPassword;

    @NotEmpty(messageResId = R.string.validation_empty)
    @ConfirmPassword(messageResId = R.string.validation_password_confirm)
    EditText editTextConfirmPassword;

    @NotEmpty(messageResId = R.string.validation_empty)
    @Length(min = 9, max = 10, messageResId = R.string.validation_length_10)
    @PhoneThai(messageResId = R.string.validation_phone)
    EditText editTextTelephoneNumber;

    Button btnRegister;

    Validator validator;

    MemberController memberController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initInstances(view);

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
        btnRegister = (Button) root.findViewById(R.id.btnRegister);
        editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) root.findViewById(R.id.editTextConfirmPassword);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        editTextTelephoneNumber = (EditText) root.findViewById(R.id.editTextTelephoneNumber);

        validator = new Validator(this);

        busProvider = BusProvider.getInstance(getActivity());
        memberController = new MemberController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        validator.setValidationListener(validationListener);
        btnRegister.setOnClickListener(onClickListener);
    }

    private void register() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String telephoneNumber = editTextTelephoneNumber.getText().toString();
        Member member = new Member(email, password, telephoneNumber);
        memberController.register(member);
    }

    @Subscribe
    public void registerSubscribe(MemberEvent.Register registerEvent) {
        if (registerEvent.isSuccess()) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
            Toast.makeText(getActivity(), getString(R.string.register_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.register_fail), Toast.LENGTH_SHORT).show();
        }
    }

    Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override
        public void onValidationSucceeded() {
            register();
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
                } else if (error.getView().equals(editTextConfirmPassword)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Confirm Pass: " + rule.getMessage(getActivity()) + "\n";
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

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            validator.validate();
        }
    };

}
