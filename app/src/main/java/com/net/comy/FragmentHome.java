package com.net.comy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
  private Context mContext;
  private RecyclerView mCategoryRecView;
  private CategoryAdapter mCategoryAdapter;

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
    return inflater.inflate(R.layout.fragment_home, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    ArrayList<Category> categoryArrayList = new ArrayList<>();
    categoryArrayList.add(
        new Category("Banking", R.drawable.ic_banking));
    categoryArrayList.add(
        new Category("E-commerce", R.drawable.ic_ecommerce));
    categoryArrayList.add(
        new Category("Telecom & Internet", R.drawable.ic_telecommunications));
    categoryArrayList.add(
        new Category("Hotels", R.drawable.ic_hotel));
    categoryArrayList.add(
        new Category("Government departments", R.drawable.ic_government));
    categoryArrayList.add(
        new Category("Restaurants & Cafes", R.drawable.ic_restaurant));

      mCategoryAdapter = new CategoryAdapter(mContext,categoryArrayList);
    mCategoryRecView = view.findViewById(R.id.home_recycler_view);
    mCategoryRecView.setLayoutManager(new GridLayoutManager(mContext, 3));
    mCategoryRecView.setAdapter(mCategoryAdapter);
  }
}
