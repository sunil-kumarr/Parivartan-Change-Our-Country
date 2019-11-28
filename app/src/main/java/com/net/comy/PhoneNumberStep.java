package com.net.comy;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ernestoyaquello.com.verticalstepperform.Step;

public class PhoneNumberStep extends Step<String> {

    private EditText mobileNumber, verification_code;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private LinearLayout mLinearLayout;
    PhoneVerification phoneVerification;
    private Button mSendCode, mVerify, mResend;

    public PhoneNumberStep(String stepTitle,String subTitle) {
        super(stepTitle,subTitle);
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected View createStepContentLayout() {
        mLinearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.username_step, null, false);
        mobileNumber = mLinearLayout.findViewById(R.id.edt_mobile_number);
        verification_code = mLinearLayout.findViewById(R.id.edt_verification_code);
        mSendCode = mLinearLayout.findViewById(R.id.send_code_btn);
        mVerify = mLinearLayout.findViewById(R.id.verify_btn);
        mResend = mLinearLayout.findViewById(R.id.resend_btn);

        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View pView) {
                String code = verification_code.getText().toString();
                if (!TextUtils.isEmpty(code)) {
                    phoneVerification.verifyCode(code);
                } else {
                    verification_code.setError("wrong code");
                }

            }
        });

        mSendCode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        phoneVerification = (PhoneVerification) getContext();
                        String number = getStepData();
                        if (number.length() == 10) {
                            String realNum = "+91" + number;
                            phoneVerification.startPhoneNumberVerification(realNum);
                            mSendCode.setVisibility(View.GONE);
                            verification_code.setVisibility(View.VISIBLE);
                            mResend.setVisibility(View.VISIBLE);
                            mVerify.setVisibility(View.VISIBLE);
                            mobileNumber.setEnabled(true);
                        } else {
                            mobileNumber.setError("Invalid Number");
                        }
                    }
                });
        mobileNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence pCharSequence, int pI, int pI1, int pI2) {

            }

            @Override
            public void onTextChanged(CharSequence pCharSequence, int pI, int pI1, int pI2) {
                markAsCompletedOrUncompleted(true);
            }

            @Override
            public void afterTextChanged(Editable pEditable) {

            }
        });
        return mLinearLayout;
    }

    @Override
    protected IsDataValid isStepDataValid(String stepData) {
        boolean isvalid =false;
        if(stepData.equals("verified")){
            isvalid=true;
        }
        return new IsDataValid(isvalid);
    }

    @Override
    public String getStepData() {
        Editable mobileNumberText = mobileNumber.getText();
        return mobileNumberText != null ? mobileNumberText.toString() : "";
    }

    @Override
    public String getStepDataAsHumanReadableString() {
        return null;
    }

    @Override
    protected void onStepOpened(boolean animated) {
//        mFirebaseUser = mFirebaseAuth.getCurrentUser();
//        if(mFirebaseUser!=null) {
//            String num = mFirebaseUser.getPhoneNumber();
//            updateTitle("Mobile Number:"+num,true);
//            getFormView().goToNextStep(true);
//            isStepDataValid(num.substring(3,13));
//            markAsCompleted(true);
//            mSendCode.setText("Update");
//        }
    }

    @Override
    protected void onStepClosed(boolean animated) {

    }

    @Override
    protected void onStepMarkedAsCompleted(boolean animated) {
        // updateTitle("Mobile Number:"+getStepData(),true);
        isStepDataValid("verified");
        onStepClosed(true);

        // This will be called automatically whenever the step is marked as completed.
    }

    @Override
    protected void onStepMarkedAsUncompleted(boolean animated) {
        // This will be called automatically whenever the step is marked as uncompleted.
    }

    @Override
    public void restoreStepData(String stepData) {
        // To restore the step after a configuration change, we restore the text of its EditText view.
//        mobileNumber.setText(stepData);
    }

    public interface PhoneVerification {
        void startPhoneNumberVerification(String phonenumber);

        void verifyCode(String code);
    }
}
