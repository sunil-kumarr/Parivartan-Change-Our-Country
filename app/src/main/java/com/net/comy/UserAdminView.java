package com.net.comy;

public class UserAdminView {
    String firebaseID;
    boolean isAdmin;

    public UserAdminView() {
    }

    public String getFirebaseID() {
        return firebaseID;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean pAdmin) {
        isAdmin = pAdmin;
    }

    public void setFirebaseID(String pFirebaseID) {
        firebaseID = pFirebaseID;
    }
}
