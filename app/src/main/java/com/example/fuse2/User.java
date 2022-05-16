package com.example.fuse2;

import java.io.Serializable;

public class User implements Serializable {
    protected  String email;
    protected  String password;
    protected String userId;

    //default constructor
    public  User(){
        this.email = "";
        this.password = "";

    }
    //parametrized constructor
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    //getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
