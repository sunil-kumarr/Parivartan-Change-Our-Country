package com.net.comy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {
    private EditText mEmail,mPass,mName;
    private FirebaseAuth mFirebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAuth = FirebaseAuth.getInstance();
        mEmail = findViewById(R.id.edt_email_address);
        mPass = findViewById(R.id.edt_password);
        mName = findViewById(R.id.edt_name);
    }

    public void singUpUserWithEmail(View pView){
        String email = mEmail.getText().toString();
        String pass = mPass.getText().toString();
        String name = mName.getText().toString();

//        mFirebaseAuth.createUserWithEmailAndPassword(, );
    }
}
