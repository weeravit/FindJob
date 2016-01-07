package com.weeravit_it.findjob.findjob.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.MainActivity;
import com.weeravit_it.findjob.findjob.activity.RegisterActivity;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.MemberEvent;
import com.weeravit_it.findjob.findjob.controller.MemberController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Member;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    @NotEmpty(messageResId = R.string.validation_empty)
    @Email(messageResId = R.string.validation_email)
    EditText editTextEmail;

    @NotEmpty(messageResId = R.string.validation_empty)
    EditText editTextPassword;

    Button btnLogin;
    Button btnRegister;
    TextView tvSkip;

    Validator validator;

    MemberController memberController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        initInstances(view);
        policy();

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
        btnLogin = (Button) root.findViewById(R.id.btnLogin);
        btnRegister = (Button) root.findViewById(R.id.btnRegister);
        editTextPassword = (EditText) root.findViewById(R.id.editTextPassword);
        editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        tvSkip = (TextView) root.findViewById(R.id.tvSkip);

        validator = new Validator(this);

        busProvider = BusProvider.getInstance(getActivity());
        memberController = new MemberController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        validator.setValidationListener(validationListener);
        btnLogin.setOnClickListener(onClickListener);
        btnRegister.setOnClickListener(onClickListener);
        tvSkip.setOnClickListener(onClickListener);
    }

    private void policy() {
        if (LocalStorage.getInstance().getMember() != null) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }

    private Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override
        public void onValidationSucceeded() {
            login();
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            String msgError = "";
            for (ValidationError error : errors) {
                if (error.getView().equals(editTextEmail)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Email: " + rule.getMessage(getActivity()) + "\n";
                    }
                } else {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Password: " + rule.getMessage(getActivity()) + "\n";
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnLogin) {
                validator.validate();
            } else if (v == btnRegister) {
                Intent intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }
    };

    private void login() {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        Member member = new Member(email, password);
        memberController.login(member);
    }

    @Subscribe
    public void loginSubscribe(MemberEvent.Login loginEvent) {
        if (loginEvent.isSuccess()) {
            startActivity(new Intent(getActivity(), MainActivity.class));
            Toast.makeText(getActivity(), getString(R.string.login_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.login_fail), Toast.LENGTH_SHORT).show();
        }
    }

}
