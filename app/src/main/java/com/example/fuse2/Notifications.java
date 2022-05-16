package com.example.fuse2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fuse2.databinding.ActivityNotificationsBinding;

public class Notifications extends DrawerBase {

    ActivityNotificationsBinding activityNotificationsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        activityNotificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Notifications");

        setContentView(activityNotificationsBinding.getRoot());
    }
}