package com.net.comy;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private Context mContext;
    private String mUserId;
    private ArrayList<ChatMessageModel> mChatMessageModelArrayList;

    public ChatAdapter(Context pContext, String pUserId) {
        mContext = pContext;
        mUserId = pUserId;
        mChatMessageModelArrayList = new ArrayList<>();
    }

    public void addChatMessage(ChatMessageModel pMessageModel) {
        mChatMessageModelArrayList.add(pMessageModel);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.chat_message, parent, false);
        return new ChatViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.createMessageBox(position);
    }

    @Override
    public int getItemCount() {
        return mChatMessageModelArrayList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private TextView mChatMessage, mChatTime;
        private CardView mChatContainer;
        private LinearLayout mLinearLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            mChatMessage = itemView.findViewById(R.id.chat_message_txt);
            mChatTime = itemView.findViewById(R.id.chat_message_time_txt);
            mChatContainer = itemView.findViewById(R.id.chat_container);
            mLinearLayout = itemView.findViewById(R.id.linearlayout_cont);
        }

        public void createMessageBox(int pPosition) {
            ChatMessageModel messageModel = mChatMessageModelArrayList.get(pPosition);
            mChatMessage.setText(messageModel.getChatMessage());
            String time = Utils.getMessageTime(messageModel.getTimestamp());
            mChatTime.setText(time);
            if (mUserId.equals(messageModel.getUserId())) {
                mChatContainer.setBackgroundResource(R.drawable.chat_message_background);
                mChatMessage.setTextColor(mContext.getResources().getColor(R.color.white));
                mChatTime.setTextColor(mContext.getResources().getColor(R.color.white));
                mLinearLayout.setGravity(Gravity.END);
                mLinearLayout.setPadding(dpToPx(50), 0, 0, 0);
            } else {
                mChatContainer.setBackgroundResource(R.drawable.chat_message_background_normal);
                mChatMessage.setTextColor(mContext.getResources().getColor(R.color.black));
                mChatTime.setTextColor(mContext.getResources().getColor(R.color.black));
                mLinearLayout.setGravity(Gravity.START);
                mLinearLayout.setPadding(0, 0, dpToPx(50), 0);
            }
        }
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
