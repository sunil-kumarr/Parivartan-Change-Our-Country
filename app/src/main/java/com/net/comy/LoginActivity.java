package com.net.comy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ui.phone.PhoneVerification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class LoginActivity extends AppCompatActivity
        implements StepperFormListener,
        GoogleSignInStep.GoogleInter,
        PhoneNumberStep.PhoneVerification {

    private static final int RC_SIGN_IN = 134;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    private PhoneNumberStep userNameStep;
    private GoogleSignInStep mGoogleSignInStep;
    private VerticalStepperFormView verticalStepperForm;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private String mobileNumber;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Create the steps.
        mFirebaseAuth = FirebaseAuth.getInstance();
        userNameStep = new PhoneNumberStep("Verify Mobile Number(Required)", "Enter 10 digit mobile number.");
        mGoogleSignInStep =
                new GoogleSignInStep(
                        "Google SignIn (Required)",
                        "Allows us to verify that you are a genuine user.",
                        "SignIn");
        verticalStepperForm = findViewById(R.id.stepper_form);
        verticalStepperForm
                .setup(this, mGoogleSignInStep, userNameStep)
                .allowNonLinearNavigation(false)
                .displayStepButtons(false)
                .confirmationStepTitle("Verification Complete")
                .lastStepNextButtonText("Let's get started.")
                .init();
        verticalStepperForm.completeForm();
        final Button resendCode = verticalStepperForm.findViewById(R.id.resend_btn);
        resendCode.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        resendVerificationCode(mobileNumber, mResendToken);
                    }
                });

        mCallbacks =
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential credential) {

                        mVerificationInProgress = false;
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        mVerificationInProgress = false;
                        if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(LoginActivity.this, "Invalid Mobile Number", Toast.LENGTH_SHORT)
                                    .show();
                        } else if (e instanceof FirebaseTooManyRequestsException) {
                            Toast.makeText(LoginActivity.this, "SMS quota Exceeded", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCodeSent(
                            @NonNull String verificationId,
                            @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        Toast.makeText(LoginActivity.this, "Code sent for verification", Toast.LENGTH_SHORT)
                                .show();
                        mVerificationId = verificationId;
                        mResendToken = token;
                    }
                };
    }

    @Override
    public void onCompletedForm() {

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onCancelledForm() {
        // This method will be called when the user clicks on the cancel button of the form.
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                verticalStepperForm.markOpenStepAsCompletedOrUncompleted(true);
            }
        }
    }

    @Override
    public void googleSignIn() {
        List<AuthUI.IdpConfig> providers =
                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
                RC_SIGN_IN);
    }


    @Override
    public void onStart() {
        super.onStart();
        mCurrentUser = mFirebaseAuth.getCurrentUser();
        if (mCurrentUser != null ) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        if (mVerificationInProgress) {
            EditText editText = verticalStepperForm.findViewById(R.id.edt_mobile_number);
            startPhoneNumberVerification(editText.getText().toString());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }

    @Override
    public void startPhoneNumberVerification(String phoneNumber) {
        mobileNumber = phoneNumber;
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phoneNumber, // Phone number to verify
                        60, // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        this, // Activity (for callback binding)
                        mCallbacks); // OnVerificationStateChangedCallbacks
        mVerificationInProgress = true;
    }

    @Override
    public void verifyCode(String code) {
        verifyPhoneNumberWithCode(mVerificationId, code);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(
            String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance()
                .verifyPhoneNumber(
                        phoneNumber, // Phone number to verify
                        60, // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        this, // Activity (for callback binding)
                        mCallbacks, // OnVerificationStateChangedCallbacks
                        token); // ForceResendingToken from callbacks
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mCurrentUser = mFirebaseAuth.getCurrentUser();
            mCurrentUser.updatePhoneNumber(credential)
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void pVoid) {
                            verticalStepperForm.markStepAsCompleted(1, true);
                            verticalStepperForm.goToNextStep(true);
                            Toast.makeText(LoginActivity.this, "Successfully logged In.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception pE) {
                    Toast.makeText(LoginActivity.this, "Failed :" + pE.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
