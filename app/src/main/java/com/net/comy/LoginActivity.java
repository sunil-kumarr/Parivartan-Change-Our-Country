package com.net.comy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN_GOOGLE =  134;
    private ImageView mMobileLogin,mGogoleSign,mMailSingUp;
    private Button mLginButton;
    private EditText mPassword,mEmail;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mCurrentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPassword = findViewById(R.id.edt_password);
        mEmail = findViewById(R.id.edt_email_address);
        mLginButton = findViewById(R.id.login_button);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mLginButton.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View pView) {
            String email = mEmail.getText().toString();
            String pass = mPassword.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
              Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT)
                  .show();
            } else {
              mFirebaseAuth
                  .createUserWithEmailAndPassword(email, pass)
                  .addOnCompleteListener(
                      LoginActivity.this,
                      new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> pTask) {
                          if (pTask.isSuccessful()) {
                            mCurrentUser = mFirebaseAuth.getCurrentUser();
                          } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                          }
                        }
                      });
            }
          }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mFirebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    public void googleSingIn(View pView){
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.GoogleBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN_GOOGLE);
    }
    public void phoneNumberSignIn(View pView){
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.PhoneBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN_GOOGLE);
    }
    public  void emailSingUp(View pView){
       startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Sign Failed!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
