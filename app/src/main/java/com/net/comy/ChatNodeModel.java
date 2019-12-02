package com.net.comy;

import java.util.ArrayList;

public class ChatNodeModel {
    String messageOwnerId;
    ChatMessageModel mLastMessage;


    public ChatNodeModel() {
    }

    public ChatNodeModel(String pMessageOwnerId, ChatMessageModel pLastMessage) {
        messageOwnerId = pMessageOwnerId;
        mLastMessage = pLastMessage;
    }

    public String getMessageOwnerId() {
        return messageOwnerId;
    }

    public void setMessageOwnerId(String pMessageOwnerId) {
        messageOwnerId = pMessageOwnerId;
    }

    public ChatMessageModel getLastMessage() {
        return mLastMessage;
    }

    public void setLastMessage(ChatMessageModel pLastMessage) {
        mLastMessage = pLastMessage;
    }
}
