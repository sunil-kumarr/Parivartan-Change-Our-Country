package com.net.comy;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter {
    private Context mContext;
    private ArrayList<String> typesOFCategory;
    private ArrayList<Integer> images;

    public CategoryAdapter(@NonNull Context context, int resource, ArrayList<String> pTypesOFCategory, ArrayList<Integer> pImages) {
        super(context, resource);
        typesOFCategory = pTypesOFCategory;
        mContext=context;
        images = pImages;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        TextView layout = (TextView) inflater.inflate(R.layout.category_itme, parent, false);
        TextView tvLanguage = layout.findViewById(R.id.category_item_tab);
        tvLanguage.setText(typesOFCategory.get(position));
        Drawable icon = mContext.getResources().getDrawable(images.get(position));
        tvLanguage.setCompoundDrawables(icon,null,null,null);
        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public int getCount() {
        return typesOFCategory.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
