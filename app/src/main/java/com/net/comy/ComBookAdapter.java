package com.net.comy;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ComBookAdapter extends FragmentStatePagerAdapter {
  private final List<Fragment> mFragmentList = new ArrayList<>();
  private final List<String> mFragmentTitleList = new ArrayList<>();
  private final List<Integer> mFragmentIconList = new ArrayList<>();
  private Context context;

  ComBookAdapter(FragmentManager fm, Context context) {
    super(fm);
    this.context = context;
  }

  @Override
  public Fragment getItem(int position) {
    return mFragmentList.get(position);
  }

  public void addFragment(Fragment fragment, String title) {
    mFragmentList.add(fragment);
    mFragmentTitleList.add(title);
    //        mFragmentIconList.add(tabIcon);
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return mFragmentTitleList.get(position);
  }

  @Override
  public int getCount() {
    return mFragmentList.size();
  }
  //    public View getTabView(int position) {
  //        View view = LayoutInflater.from(context).inflate(R.layout.custom_tabl_tablayout, null);
  //        TextView tabTextView = view.findViewById(R.id.tabTextView);
  //        tabTextView.setText(mFragmentTitleList.get(position));
  //        return view;
  //    }
  //    public View getSelectedTabView(int position) {
  //        View view = LayoutInflater.from(context).inflate(R.layout.custom_tabl_tablayout, null);
  //        TextView tabTextView = view.findViewById(R.id.tabTextView);
  //        tabTextView.setText(mFragmentTitleList.get(position));
  //        tabTextView.setTextColor(ContextCompat.getColor(context, R.color.white));
  //        CardView cardView = view.findViewById(R.id.tab_Card_view);
  //        cardView.setBackgroundResource(R.drawable.border_fill);
  //        return view;
  //    }
}
