package com.net.comy;

public class ChatMessageModel {
    private String mChatMessage;
    private String mUserId;
    private long timestamp;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String pChatMessage, String pUserId, long pTimestamp) {
        mChatMessage = pChatMessage;
        mUserId = pUserId;
        timestamp = pTimestamp;
    }

    public String getChatMessage() {
        return mChatMessage;
    }

    public void setChatMessage(String pChatMessage) {
        mChatMessage = pChatMessage;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String pUserId) {
        mUserId = pUserId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long pTimestamp) {
        timestamp = pTimestamp;
    }
}
