package com.example.fuse2;

public class Notification {

    String message, receiver, sender;

    public Notification(String message, String reciever, String sender) {
        this.message = message;
        this.receiver = reciever;
        this.sender = sender;
    }

    public Notification() {
        this.message = "message";
        this.receiver = "receiver";
        this.sender = "sender";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "message='" + message + '\'' +
                ", receiver='" + receiver + '\'' +
                ", sender='" + sender + '\'' +
                '}';
    }
}
