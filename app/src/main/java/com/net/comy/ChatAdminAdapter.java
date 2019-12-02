package com.net.comy;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdminAdapter extends RecyclerView.Adapter<ChatAdminAdapter.ChatViewHolder> {
    private Context mContext;
    private ArrayList<ChatNodeModel> mCustomers;

    public ChatAdminAdapter(Context pContext) {
        mContext = pContext;
        mCustomers = new ArrayList<>();
    }

    public void addChatMessage(ChatNodeModel pChatNodeModel) {
        Toast.makeText(mContext, "count me in", Toast.LENGTH_SHORT).show();
        mCustomers.add(pChatNodeModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.chat_admin_customer_layout, parent, false);
        return new ChatViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.createMessageTab(position);
    }

    @Override
    public int getItemCount() {
        Toast.makeText(mContext, ""+mCustomers.size(), Toast.LENGTH_SHORT).show();
        return mCustomers.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView mChatMessage, mChatOwner;
        private ConstraintLayout mChatContainer;
        private CircleImageView mCustomerImage;
        private ImageView mStatus;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatMessage = itemView.findViewById(R.id.customer_last_message);
            mChatContainer = itemView.findViewById(R.id.chat_tab_container);
            mCustomerImage = itemView.findViewById(R.id.customer_image);
            mChatOwner = itemView.findViewById(R.id.customer_name);
            mStatus = itemView.findViewById(R.id.message_status);
        }

        public void createMessageTab(final int pPosition) {
            ChatNodeModel nodeModel = mCustomers.get(pPosition);
            if (nodeModel != null) {
                ChatMessageModel mLastMessage = nodeModel.getLastMessage();
                String name = nodeModel.getOwnerName();
                String image = nodeModel.getOwnerImageUrl();
                final String userId = nodeModel.getMessageOwnerId();
                mChatMessage.setText(mLastMessage.getChatMessage());
                if (mLastMessage.isRead()) {
                    mStatus.setVisibility(View.INVISIBLE);
                } else {
                    mStatus.setVisibility(View.VISIBLE);
                }
                if (name != null) {
                    mChatOwner.setText(name);
                }
                if (image != null) {
                    Picasso.get()
                            .load(image)
                            .fit()
                            .placeholder(R.drawable.ic_customer)
                            .into(mCustomerImage);
                }
                mChatContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View pView) {
                        Intent intent = new Intent(mContext,ChatPingActivity.class);
                        intent.putExtra("user_id",userId);
                        mContext.startActivity(intent);
                    }
                });
            }
        }
    }
}

