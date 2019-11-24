package com.net.comy;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
  private ComBookAdapter adapter;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private FirebaseAuth mFirebaseAuth;
  private FloatingActionButton mRegisterComplaint;
  private FirebaseUser mCurrentUser;
  private Context mContext;
  private int[] tabIcons = {
          R.drawable.circle_black, R.drawable.circle_black, R.drawable.circle_black
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mFirebaseAuth = FirebaseAuth.getInstance();
    mCurrentUser = mFirebaseAuth.getCurrentUser();
    mRegisterComplaint = findViewById(R.id.register_com_button);
    tabLayout = findViewById(R.id.tabLayout);
    viewPager = findViewById(R.id.viewpager);
    adapter = new ComBookAdapter(getSupportFragmentManager(),MainActivity.this);
    adapter.addFragment(new FragmentComplainAll(), "All");
    adapter.addFragment(new FragmentComplainOpen(), "Open");
    adapter.addFragment(new FragmentComplainClosed(), "Close");
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
    mRegisterComplaint.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View pView) {
        startActivity(new Intent(MainActivity.this,RegisterComplaint.class));
      }
    });
  }
  public void signOut(View pView){
    mFirebaseAuth.signOut();
    startActivity(new Intent(MainActivity.this, LoginActivity.class));
    finish();
  }
}
