package com.weeravit_it.findjob.findjob.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.weeravit_it.findjob.findjob.R;
import com.weeravit_it.findjob.findjob.activity.MainActivity;
import com.weeravit_it.findjob.findjob.activity.ResumejobActivity;
import com.weeravit_it.findjob.findjob.bus.BusProvider;
import com.weeravit_it.findjob.findjob.bus.event.ResumeEvent;
import com.weeravit_it.findjob.findjob.controller.ResumeController;
import com.weeravit_it.findjob.findjob.manager.http.HTTPManager;
import com.weeravit_it.findjob.findjob.manager.json.GsonManager;
import com.weeravit_it.findjob.findjob.model.Job;
import com.weeravit_it.findjob.findjob.model.Resume;
import com.weeravit_it.findjob.findjob.model.Resumejob;
import com.weeravit_it.findjob.findjob.model.extra.ResumeBox;
import com.weeravit_it.findjob.findjob.model.extra.SelectedJobs;
import com.weeravit_it.findjob.findjob.utils.AppUtils;
import com.weeravit_it.findjob.findjob.utils.LocalStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.halfbit.tinybus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResumeManageFragment extends Fragment {

    @NotEmpty(messageResId = R.string.validation_empty)
    EditText editTextObjective;

    @NotEmpty(messageResId = R.string.validation_empty)
    EditText editTextEducation;

    @NotEmpty(messageResId = R.string.validation_empty)
    EditText editTextResumejob;

    ImageView ivResume;
    Toolbar toolbar;
    EditText editTextExperience;
    EditText editTextSkill;
    LinearLayout linearLayoutImage;
    Button btnCancel;
    Button btnOK;
    LinearLayout linearLayoutBtn;
    ProgressDialog progressDialog;

    ResumeBox resume;
    String method;

    final int RESUME_CODE = 99;
    final int RESUMEJOB_CODE = 69;
    final int GALLERY_CODE = 1;

    String imagePath;

    Validator validator;
    ResumeController resumeController;
    BusProvider busProvider;

    public static Fragment newInstance() {
        ResumeManageFragment fragment = new ResumeManageFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_resume_manage, container, false);

        initInstances(view);
        manage();

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
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        editTextSkill = (EditText) root.findViewById(R.id.editTextSkill);
        editTextEducation = (EditText) root.findViewById(R.id.editTextEducation);
        editTextExperience = (EditText) root.findViewById(R.id.editTextExperience);
        editTextObjective = (EditText) root.findViewById(R.id.editTextObjective);
        editTextResumejob = (EditText) root.findViewById(R.id.editTextResumeJob);
        linearLayoutBtn = (LinearLayout) root.findViewById(R.id.linearLayoutBtn);
        btnOK = (Button) root.findViewById(R.id.btnOK);
        btnCancel = (Button) root.findViewById(R.id.btnCancel);
        linearLayoutImage = (LinearLayout) root.findViewById(R.id.linearLayoutImage);
        ivResume = (ImageView) root.findViewById(R.id.ivResume);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.setCancelable(false);

        validator = new Validator(this);
        validator.setValidationListener(validationListener);

        busProvider = BusProvider.getInstance(getActivity());
        resumeController = new ResumeController(getActivity(), busProvider, HTTPManager.getInstance(), GsonManager.getInstance());

        btnOK.setOnClickListener(onClickListener);
        btnCancel.setOnClickListener(onClickListener);
        linearLayoutImage.setOnClickListener(onClickListener);
        editTextResumejob.setOnClickListener(onClickListener);
    }

    private void manage() {
        method = getActivity().getIntent().getStringExtra(getString(R.string.intent_key));
        if (method.equals(getString(R.string.intent_add))) {
            addMode();
        } else {
            showResume(getActivity().getIntent().getIntExtra(getString(R.string.intent_obj), 0));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESUMEJOB_CODE == resultCode) {
            // append text to tvResumeJob
            getResumejob();
        } else {
            // Gallery
            if (data != null) {
                // Get the Image from data
                AppUtils appUtils = new AppUtils();
                String imgPath = appUtils.getRealPathForImage(getActivity(), data.getData());
                ivResume.setImageBitmap(BitmapFactory.decodeFile(imgPath));

                imagePath = imgPath;
            }
        }
    }

    private void disableMode() {
        toolbar.setVisibility(View.VISIBLE);
        linearLayoutBtn.setVisibility(View.GONE);

        editTextObjective.setEnabled(false);
        editTextExperience.setEnabled(false);
        editTextEducation.setEnabled(false);
        editTextSkill.setEnabled(false);
    }

    private void enableMode() {
        toolbar.setVisibility(View.GONE);
        linearLayoutBtn.setVisibility(View.VISIBLE);

        editTextObjective.setEnabled(true);
        editTextExperience.setEnabled(true);
        editTextEducation.setEnabled(true);
        editTextSkill.setEnabled(true);
    }

    private void addMode() {
        enableMode();
    }

    public void editMode() {
        method = getString(R.string.intent_edit);
        enableMode();
    }

    private void showDetailOnUI() {
        Resume resume = this.resume.getResume();

        Glide.with(getActivity()).load(this.resume.getImage()).into(ivResume);
        editTextObjective.setText(Html.fromHtml(resume.getObjective()));
        editTextExperience.setText(Html.fromHtml(resume.getExperience()));
        editTextEducation.setText(Html.fromHtml(resume.getEducation()));
        editTextSkill.setText(Html.fromHtml(resume.getSkill()));

        List<Job> jobList = new ArrayList<>();
        Iterator<Resumejob> resumejobIterator = this.resume.getResumejobs().iterator();
        while (resumejobIterator.hasNext()) {
            jobList.add(resumejobIterator.next().getJob());
        }
        SelectedJobs.getInstance().setJobs(jobList);
        getResumejob();
    }

    private String getDataResume() {
        if (resume == null)
            resume = new ResumeBox();
        resume.getResume().setExperience(editTextExperience.getText().toString());
        resume.getResume().setEducation(editTextEducation.getText().toString());
        resume.getResume().setObjective(editTextObjective.getText().toString());
        resume.getResume().setSkill(editTextSkill.getText().toString());
        resume.getResume().setMember(LocalStorage.getInstance().getMember());

        List<Resumejob> resumejobs = new ArrayList<>();
        Iterator<Job> jobIterator = SelectedJobs.getInstance().getJobs().iterator();
        while (jobIterator.hasNext()) {
            Resumejob resumejob = new Resumejob(jobIterator.next());
            resumejobs.add(resumejob);
        }

        resume.setResumejobs(resumejobs);

        return GsonManager.getInstance().getGson().toJson(resume);
    }

    private void getResumejob() {
        String text = "";
        Iterator<Job> jobs = SelectedJobs.getInstance().getJobs().iterator();
        while (jobs.hasNext())
            text += jobs.next().getName() + "\n";
        editTextResumejob.setText(text);
    }

    private void addResume() {
        RequestBody image = null;
        if (imagePath != null)
            image = RequestBody.create(MediaType.parse("image/*"), new File(imagePath));
        RequestBody resumeData = RequestBody.create(MediaType.parse("application/json"), getDataResume());
        resumeController.store(image, resumeData);
    }

    private void editResume() {
        RequestBody image = null;
        if (imagePath != null)
            image = RequestBody.create(MediaType.parse("image/*"), new File(imagePath));
        RequestBody resumeData = RequestBody.create(MediaType.parse("application/json"), getDataResume());
        resumeController.update(image, resumeData);
    }

    public void deleteResume() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.resume_dialog_content));
        builder.setPositiveButton(getString(R.string.resume_dialog_button_confirm), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resumeController.delete(resume);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.resume_dialog_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showResume(int id) {
        progressDialog.show();
        resumeController.show(id);
    }

    @Subscribe
    public void showSubscribe(ResumeEvent.Show show) {
        if (show.isSuccess()) {
            this.resume = show.getResumeBox();
            showDetailOnUI();
        } else {
            Toast.makeText(getActivity(), getString(R.string.status_fail), Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Subscribe
    public void storeSubscribe(ResumeEvent.Store store) {
        if (store.isSuccess()) {
            this.resume = store.getResumeBox();
            disableMode();
            Toast.makeText(getActivity(), getString(R.string.resume_add_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.resume_manage_fail), Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Subscribe
    public void updateSubscribe(ResumeEvent.Update update) {
        if (update.isSuccess()) {
            this.resume = update.getResumeBox();
            disableMode();
            Toast.makeText(getActivity(), getString(R.string.resume_edit_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.resume_manage_fail), Toast.LENGTH_SHORT).show();
        }
        progressDialog.dismiss();
    }

    @Subscribe
    public void deleteSubscribe(ResumeEvent.Delete delete) {
        if (delete.isSuccess()) {
            getActivity().finish();
            Toast.makeText(getActivity(), getString(R.string.resume_delete_success), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), getString(R.string.resume_manage_fail), Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == btnCancel) {
                if (method.equals(getString(R.string.intent_add))) {
                    getActivity().finish();
                } else {
                    disableMode();
                }
            } else if (v == editTextResumejob) {
                Intent intent = new Intent(getActivity(), ResumejobActivity.class);
                startActivityForResult(intent, RESUMEJOB_CODE);
            } else if (v == linearLayoutImage) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALLERY_CODE);
            } else {
                // Button Save.
                validator.validate();
            }
        }
    };

    Validator.ValidationListener validationListener = new Validator.ValidationListener() {
        @Override
        public void onValidationSucceeded() {
            progressDialog.show();
            if (method.equals(getString(R.string.intent_add))) {
                addResume();
            } else {
                editResume();
            }
        }

        @Override
        public void onValidationFailed(List<ValidationError> errors) {
            String msgError = "";
            for (ValidationError error : errors) {
                if (error.getView().equals(editTextObjective)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Objective: " + rule.getMessage(getActivity()) + "\n";
                    }
                } else if (error.getView().equals(editTextEducation)) {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Education: " + rule.getMessage(getActivity()) + "\n";
                    }
                } else {
                    for (Rule rule : error.getFailedRules()) {
                        msgError += "Job: " + rule.getMessage(getActivity()) + "\n";
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

}
