package com.example.exfirebaselogin.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String userName;
    private String phoneNumber;
    private Timestamp timestampCreated;
    private String userId;

    public UserModel() {

    }

    public UserModel(String userName, String phoneNumber, Timestamp now, String userId) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.timestampCreated = now;
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userName='" + userName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", timestampCreated=" + timestampCreated +
                ", userId='" + userId + '\'' +
                '}';
    }
}
