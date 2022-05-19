package com.example.fuse2;

public class Message {

    private String sender, receiver, content, time, date;

    public Message(String sender, String receiver, String content, String time, String date) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.time = time;
        this.date = date;
    }
    public Message() {
        this.sender = "sender";
        this.receiver = "receiver";
        this.content = "content";
        this.time = "time";
        this.date = "date";
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
