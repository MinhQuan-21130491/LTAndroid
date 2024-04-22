package com.example.exfirebaselogin.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatRoomModel {
    private String chatRoomId;
    private List<String> userIds;
    private Timestamp lastMessageTimestamp;
    private String lastMessageSender;
    private String lastMessage;

    public ChatRoomModel(String chatRoomId, List<String> userIds, Timestamp lastMessageTimestamp, String lastMessageSender, String lassMessage) {
        this.chatRoomId = chatRoomId;
        this.userIds = userIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSender = lastMessageSender;
        this.lastMessage = lassMessage;
    }

    public ChatRoomModel() {
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSender() {
        return lastMessageSender;
    }

    public void setLastMessageSender(String lastMessageSender) {
        this.lastMessageSender = lastMessageSender;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public String toString() {
        return "ChatRoomModel{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", userIds=" + userIds +
                ", lastMessageTimestamp=" + lastMessageTimestamp +
                ", lastMessageSender='" + lastMessageSender + '\'' +
                '}';
    }
}
