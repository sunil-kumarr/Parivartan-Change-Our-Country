package com.net.comy;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context mContext;
    private ArrayList<Category> mCategories;

    public CategoryAdapter(Context pContext, ArrayList<Category> pCategories) {
        mContext = pContext;
        mCategories = pCategories;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categor_tab_iteme,parent,false);
        return new  CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
      Category category = mCategories.get(position);
      holder.mTextView.setText(category.getTitle());
      holder.mImageView.setImageResource(category.getImage());
      holder.mImageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View pView) {
              mContext.startActivity(new Intent(mContext,RegisterComplaint.class));
          }
      });
    }

    @Override
    public int getItemCount() {
        return mCategories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        private TextView mTextView;
        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.category_image);
            mTextView = itemView.findViewById(R.id.category_title);
        }
    }
}
