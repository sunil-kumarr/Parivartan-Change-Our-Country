package com.net.comy;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.dynamic.SupportFragmentWrapper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
  private BottomNavigationView mBottomNavigationView;
  private FragmentManager mFragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mBottomNavigationView = findViewById(R.id.bottom_app_bar);
    mFragmentManager = getSupportFragmentManager();
    mBottomNavigationView.setOnNavigationItemSelectedListener(
        new BottomNavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem pMenuItem) {
            Fragment fragment = null;
            switch (pMenuItem.getItemId()) {
              case R.id.nav_home:
                fragment = new FragmentHome();
                break;
              case R.id.nav_complaints:
                fragment = new FragmentComBook();
                break;
              case R.id.nav_profile:
                fragment = new FragmentProfile();
                break;
              default:
                fragment = new FragmentHome();
            }
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment, "frag");
            fragmentTransaction.commit();
            return true;
          }
        });
  }
}
