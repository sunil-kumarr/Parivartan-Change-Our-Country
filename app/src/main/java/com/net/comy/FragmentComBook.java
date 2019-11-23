package com.net.comy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FragmentComBook extends Fragment {
  private ComBookAdapter adapter;
  private TabLayout tabLayout;
  private ViewPager viewPager;
  private FirebaseAuth mFirebaseAuth;
  private FirebaseUser mCurrentUser;
  private Context mContext;
  private int[] tabIcons = {
    R.drawable.circle_black, R.drawable.circle_black, R.drawable.circle_black
  };

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    mContext = context;
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_complaint_book, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mFirebaseAuth = FirebaseAuth.getInstance();
    mCurrentUser = mFirebaseAuth.getCurrentUser();
    tabLayout = view.findViewById(R.id.tabLayout);
    viewPager = view.findViewById(R.id.viewpager);
    adapter = new ComBookAdapter(getFragmentManager(), mContext);
    adapter.addFragment(new FragmentComplainAll(), "All");
    adapter.addFragment(new FragmentComplainOpen(), "Open");
    adapter.addFragment(new FragmentComplainClosed(), "Close");
    viewPager.setAdapter(adapter);
    tabLayout.setupWithViewPager(viewPager);
  }
}
