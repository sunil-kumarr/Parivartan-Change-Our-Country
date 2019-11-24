package com.net.comy;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {
    List<ComplaintModel> mComplaintModelList;
    Context mContext;
    Integer mToken;

    public HomeAdapter(Context pContext, int token) {
        mComplaintModelList = new ArrayList<>();
        mContext = pContext;
        mToken = token;
    }

    public void addComplainFromFirebase(ComplaintModel pComplaintModel) {
        mComplaintModelList.add(pComplaintModel);
      //  Toast.makeText(mContext, "" + mComplaintModelList.size(), Toast.LENGTH_SHORT).show();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.complain_item, parent, false);
        return new HomeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeHolder holder, int position) {
        holder.update(mComplaintModelList.get(position));
    }

    @Override
    public int getItemCount() {
        return mComplaintModelList.size();
    }

    class HomeHolder extends RecyclerView.ViewHolder {
        TextView category, title, desc, requestID, dataTime;
        ImageView mImageView;
        ProgressBar mProgressBar;

        HomeHolder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.complain_category);
            title = itemView.findViewById(R.id.complain_title);
            desc = itemView.findViewById(R.id.complain_Desc);
            requestID = itemView.findViewById(R.id.complain_ref_id);
            dataTime = itemView.findViewById(R.id.complain_time);
            mImageView = itemView.findViewById(R.id.complain_image_preview);
            mProgressBar = itemView.findViewById(R.id.complain_image_preview_progressbar);
        }

        void update(ComplaintModel pComplaintModel) {
            if (pComplaintModel.getComplaintImage() == null) {
                mImageView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            } else {
                Picasso.get()
                        .load(pComplaintModel.getComplaintImage())
                        .fit()
                        .into(mImageView,new Callback(){

                            @Override
                            public void onSuccess() {
                                mProgressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
            title.setText(pComplaintModel.getComplaintTitle());
            desc.setText(pComplaintModel.getComplaintDetails());
            requestID.setText(String.format("Request ID: %s", pComplaintModel.getRequestId()));
            dataTime.setText(String.format("Date: %s", Utils.getDateTime(pComplaintModel.getTimestamp())));
            category.setText(pComplaintModel.getComplaintCategory());

        }
    }
}
