package com.net.comy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.Step;

public class GoogleSignInStep extends Step<Boolean> {

    private static final Integer RC_SIGN_IN = 123;
    private Button googleBtn;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleInter mGoogleInter;

    public GoogleSignInStep(String stepTitle,String subTitle,String nextText) {
        super(stepTitle,subTitle,nextText);
        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    @Override
    public Boolean getStepData() {

        return null;
    }


    @Override
    public String getStepDataAsHumanReadableString() {

        return null;
    }

    @Override
    public void restoreStepData(Boolean data) {

    }
    @Override
    protected IsDataValid isStepDataValid(Boolean stepData) {
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        boolean flag = mFirebaseUser!=null;
        if(flag){
            updateTitle("Email: "+mFirebaseUser.getEmail(),true);
            googleBtn.setVisibility(View.GONE);
            getFormView().goToNextStep(true);
        }
      return new IsDataValid(flag);
    }

    @Override
    protected View createStepContentLayout() {
        googleBtn = (Button) LayoutInflater.from(getContext()).inflate(R.layout.google_step, getFormView(),false);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                mGoogleInter = (GoogleInter)getContext();
                mGoogleInter.googleSignIn();
            }
        });
        return googleBtn;
    }
    public interface  GoogleInter{
        void googleSignIn();
    }

    @Override
    protected void onStepOpened(boolean animated) {

    }

    @Override
    protected void onStepClosed(boolean animated) {
    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        isStepDataValid(true);
        onStepClosed(true);
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {

    }

}
