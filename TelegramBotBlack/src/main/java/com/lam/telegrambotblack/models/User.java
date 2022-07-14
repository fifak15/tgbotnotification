package com.lam.telegrambotblack.models;

public class User {

    private long globalID;
    private long telegramChatID;
    private long userJIRA_ID;
    private String userStatus;
    public User() {

    }

    public User(long globalID, long telegramChatID, long userJIRA_ID, String userStatus) {
        this.globalID = globalID;
        this.telegramChatID = telegramChatID;
        this.userJIRA_ID = userJIRA_ID;
        this.userStatus = userStatus;

    }

    public void setGlobalID(long globalID) {
        this.globalID = globalID;
    }
    public void setTelegramChatID(long telegramChatID) {
        this.telegramChatID = telegramChatID;
    }
    public Long getTelegramChatID() {
        return telegramChatID;
    }
    public void setJIRA_ID(long userJIRA_ID) {
        this.userJIRA_ID = userJIRA_ID;
    }

    public void setStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getStatus() {
        return userStatus;
    }
}
