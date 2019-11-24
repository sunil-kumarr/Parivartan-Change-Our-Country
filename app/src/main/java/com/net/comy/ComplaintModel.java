package com.net.comy;

public class ComplaintModel {
    String mComplaintCategory, mHappenedAt, mComplaintTitle, mComplaintDetails, mComplaintImage,mStatus;
    String mUserID, mUserEmail, mUserNumber, mSubmitFromAddress,mRequestId;
    long timestamp;

    public ComplaintModel() {
    }

    public ComplaintModel(String pComplaintCategory, String pHappenedAt, String pComplaintTitle, String pComplaintDetails, String pComplaintImage,
                          String pUserID, String pUserEmail, String pUserNumber, String pSubmitFromAddress, String pRequestId, long pTimestamp) {
        mComplaintCategory = pComplaintCategory;
        mHappenedAt = pHappenedAt;
        mComplaintTitle = pComplaintTitle;
        mComplaintDetails = pComplaintDetails;
        mComplaintImage = pComplaintImage;
        mUserID = pUserID;
        mUserEmail = pUserEmail;
        mUserNumber = pUserNumber;
        mSubmitFromAddress = pSubmitFromAddress;
        timestamp = pTimestamp;
        mRequestId = pRequestId;
        mStatus = "open";
    }

    public String getRequestId() {
        return mRequestId;
    }

    public void setRequestId(String pRequestId) {
        mRequestId = pRequestId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String pStatus) {
        mStatus = pStatus;
    }

    public String getUserNumber() {
        return mUserNumber;
    }

    public void setUserNumber(String pUserNumber) {
        mUserNumber = pUserNumber;
    }

    public String getSubmitFromAddress() {
        return mSubmitFromAddress;
    }

    public void setSubmitFromAddress(String pSubmitFromAddress) {
        mSubmitFromAddress = pSubmitFromAddress;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long pTimestamp) {
        timestamp = pTimestamp;
    }

    public String getComplaintCategory() {
        return mComplaintCategory;
    }

    public void setComplaintCategory(String pComplaintCategory) {
        mComplaintCategory = pComplaintCategory;
    }

    public String getHappenedAt() {
        return mHappenedAt;
    }

    public void setHappenedAt(String pHappenedAt) {
        mHappenedAt = pHappenedAt;
    }

    public String getComplaintTitle() {
        return mComplaintTitle;
    }

    public void setComplaintTitle(String pComplaintTitle) {
        mComplaintTitle = pComplaintTitle;
    }

    public String getComplaintDetails() {
        return mComplaintDetails;
    }

    public void setComplaintDetails(String pComplaintDetails) {
        mComplaintDetails = pComplaintDetails;
    }

    public String getComplaintImage() {
        return mComplaintImage;
    }

    public void setComplaintImage(String pComplaintImage) {
        mComplaintImage = pComplaintImage;
    }

    public String getUserID() {
        return mUserID;
    }

    public void setUserID(String pUserID) {
        mUserID = pUserID;
    }

    public String getUserEmail() {
        return mUserEmail;
    }

    public void setUserEmail(String pUserEmail) {
        mUserEmail = pUserEmail;
    }
}
