package com.net.comy;

import java.util.ArrayList;

public class ChatNodeModel {
    String messageOwnerId,ownerName,ownerImageUrl;
    ChatMessageModel mLastMessage;


    public ChatNodeModel() {
    }

    public ChatNodeModel(String pMessageOwnerId, String pOwnerName, String pOwnerImageUrl, ChatMessageModel pLastMessage) {
        messageOwnerId = pMessageOwnerId;
        ownerName = pOwnerName;
        ownerImageUrl = pOwnerImageUrl;
        mLastMessage = pLastMessage;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String pOwnerName) {
        ownerName = pOwnerName;
    }

    public String getOwnerImageUrl() {
        return ownerImageUrl;
    }

    public void setOwnerImageUrl(String pOwnerImageUrl) {
        ownerImageUrl = pOwnerImageUrl;
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
