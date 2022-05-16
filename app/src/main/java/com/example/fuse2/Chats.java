package com.example.fuse2;

import android.os.Bundle;

import com.example.fuse2.databinding.ActivityChatsBinding;

public class Chats extends DrawerBase {

    ActivityChatsBinding activityChatsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        activityChatsBinding = ActivityChatsBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Chats");

        setContentView(activityChatsBinding.getRoot());
    }
}