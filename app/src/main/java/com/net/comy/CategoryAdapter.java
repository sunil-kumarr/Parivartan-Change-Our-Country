package com.net.comy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {
    private Context mContext;
    private ArrayList<String> categoryNames;
    private ArrayList<Integer> categoryImages;
    private ArrayList<LinearLayout> mLayoutArrayList;
    private String mSelectedItem;

    public CategoryAdapter(Context pContext, ArrayList<String> pCategoryNames, ArrayList<Integer> pCategoryImages) {
        mContext = pContext;
        categoryNames = pCategoryNames;
        categoryImages = pCategoryImages;
        mLayoutArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_complain_category, parent, false);
        return new CategoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return categoryNames.size();
    }

    public String getSelectedItem() {
        return mSelectedItem;
    }

    class CategoryHolder extends RecyclerView.ViewHolder {
        private ImageView mCategoryImage;
        private TextView mCategoryName;
        private LinearLayout mContainer;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryName = itemView.findViewById(R.id.Name);
            mCategoryImage = itemView.findViewById(R.id.Image);
            mContainer = itemView.findViewById(R.id.container_tab);
        }

        public void update(final int pPosition) {
            mCategoryName.setText(categoryNames.get(pPosition));
            mCategoryImage.setImageResource(categoryImages.get(pPosition));
            if(!mLayoutArrayList.contains(mContainer)){
                mLayoutArrayList.add(mContainer);
            }
            mContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View pView) {
                    for(LinearLayout layout:mLayoutArrayList){
                        layout.setBackgroundResource(R.drawable.category_box);
                    }
                    mContainer.setBackgroundResource(R.drawable.category_box_selected);
                    mSelectedItem = categoryNames.get(pPosition);
                }
            });

        }
    }


}
