package com.net.comy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import java.util.ArrayList;

public class RegisterComplaint extends AppCompatActivity {
    private AppCompatSpinner mCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complaint);
        mCategorySpinner = findViewById(R.id.complaint_main_category);
        ArrayList<String> names = new ArrayList<>();
        names.add("Select Complaint Categoty");
        names.add("Restaurant");
        names.add("Hotels");
        ArrayList<Integer> images = new ArrayList<>();
        images.add(R.drawable.ic_keyboard_arrow_down_black_24dp);
        images.add(R.drawable.ic_restaurant);
        images.add(R.drawable.ic_hotel);
        CategoryAdapter adapter = new CategoryAdapter(this, R.layout.category_itme, names, images);
        mCategorySpinner.setAdapter(adapter);
    }
}
