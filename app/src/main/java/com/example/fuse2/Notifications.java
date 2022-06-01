package com.example.fuse2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuse2.databinding.ActivityNotificationsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Notifications extends DrawerBase {

    ActivityNotificationsBinding activityNotificationsBinding;

    DatabaseReference notificationRef; //for notifications

    NotificationsAdapter notificationsAdapter; //for notifications

    ArrayList<HashMap> notificationlist;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        activityNotificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Notifications");

        setContentView(activityNotificationsBinding.getRoot());


        allocateActivityTitle("Reserves");


        notificationRef = FirebaseDatabase.getInstance().getReference("Reserves"); // notifications

        getData();
    }


    private void getData() {


        recyclerView = findViewById(R.id.notificationsView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        notificationlist = new ArrayList<>();
        notificationsAdapter = new NotificationsAdapter(this, notificationlist);
        recyclerView.setAdapter(notificationsAdapter);


        notificationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for( DataSnapshot dataSnapshot : snapshot.getChildren() ) {

                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();

                    if(map.get("reserveEmail").equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        notificationlist.add(map);
                    }


                    Log.e("RESERVES", "onDataChange: " + dataSnapshot.getValue());
                }
                notificationsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("RESERVE", "onCancelled: "+ error.getMessage() );

            }
        });
    }
}