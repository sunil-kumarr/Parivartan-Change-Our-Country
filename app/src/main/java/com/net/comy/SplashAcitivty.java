package com.net.comy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class SplashAcitivty extends AppCompatActivity {
    ImageView mLogo;
    Animation logoAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_acitivty);
        mLogo = findViewById(R.id.logo_icon);
        logoAnimation = AnimationUtils.loadAnimation(this,R.anim.logo_anim);
        mLogo.startAnimation(logoAnimation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                   // Toast.makeText(this, "goo", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashAcitivty.this, MainActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashAcitivty.this, LoginActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
