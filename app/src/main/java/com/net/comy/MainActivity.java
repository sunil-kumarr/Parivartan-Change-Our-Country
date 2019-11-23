package com.net.comy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
  private ConplainTypePagerAdapter adapter;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private Toolbar mToolbar;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseUser mCurrentUser;
  private int[] tabIcons = {R.drawable.circle_black,R.drawable.circle_black,R.drawable.circle_black};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    mToolbar = findViewById(R.id.toolbar);
    mFirebaseAuth = FirebaseAuth.getInstance();
    mCurrentUser = mFirebaseAuth.getCurrentUser();
    setSupportActionBar(mToolbar);
    ActionBar actionBar = getSupportActionBar();
    if(actionBar!=null){
      if(mCurrentUser!=null){
        Toast.makeText(this, "toolbar", Toast.LENGTH_SHORT).show();
        actionBar.setTitle(mCurrentUser.getDisplayName());
        actionBar.setDisplayShowTitleEnabled(true);
      }
    }
    tabLayout = findViewById(R.id.tabLayout);
    viewPager = findViewById(R.id.viewpager);
    adapter = new ConplainTypePagerAdapter(getSupportFragmentManager(), this);
    adapter.addFragment(new FragmentComplainAll(), "All",tabIcons[0]);
    adapter.addFragment(new FragmentComplainOpen(), "Open",tabIcons[1]);
    adapter.addFragment(new FragmentComplainClosed(), "Close",tabIcons[2]);
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      TabLayout.Tab tab = tabLayout.getTabAt(i);
      assert tab != null;
      tab.setCustomView(null);
      tab.setCustomView(adapter.getTabView(i));
    }
    highLightCurrentTab(0);
    viewPager.addOnPageChangeListener(
        new ViewPager.OnPageChangeListener() {
          @Override
          public void onPageScrolled(
              int position, float positionOffset, int positionOffsetPixels) {}

          @Override
          public void onPageSelected(int position) {
            highLightCurrentTab(position); // for tab change
          }

          @Override
          public void onPageScrollStateChanged(int state) {}
        });
  }

  private void highLightCurrentTab(int position) {
    for (int i = 0; i < tabLayout.getTabCount(); i++) {
      TabLayout.Tab tab = tabLayout.getTabAt(i);
      assert tab != null;
      tab.setCustomView(null);
      tab.setCustomView(adapter.getTabView(i));
    }
    TabLayout.Tab tab = tabLayout.getTabAt(position);
    assert tab != null;
    tab.setCustomView(null);
    tab.setCustomView(adapter.getSelectedTabView(position));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.action_logout) {
      signOut();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  public void signOut() {
    FirebaseAuth.getInstance().signOut();
    startActivity(new Intent(MainActivity.this, LoginActivity.class));
    finish();
  }
}
