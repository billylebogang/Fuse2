package com.example.fuse2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuse2.databinding.ActivityChatsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chats extends DrawerBase {

    ActivityChatsBinding activityChatsBinding;


    //recycle view in the ui
    RecyclerView listchats;

    //array to file with matches from db
    ArrayList arrayList;

    //recycle view adapter
    ChatsAdapter chatsAdapter;

    //database
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //Drawer related code
        activityChatsBinding = ActivityChatsBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Chats");
        setContentView(activityChatsBinding.getRoot());
        //********************************************************************************************************
        //*********************************************************************************************************

        //getting the database reference
        DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

        detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, String> me =(HashMap<String, String>) snapshot.getValue();

                Log.e("USER DEATILS", "onDataChange: "+ me.get("name") );

                //if the user profile is not null we can try to add the message thing to the db

                if(me != null){
                    try {
                        getData(me.get("name")+""+me.get("surname"));
                    }
                    catch(Exception e){
                        Log.e("Getting username", "onDataChange: "+e.getMessage() );
                    }
                    finally {
                        return;
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



    public void getData(String username){
        //ui
        listchats = findViewById(R.id.chatsView);

        listchats.setHasFixedSize(true);
        listchats.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();
        chatsAdapter = new ChatsAdapter(this, arrayList);
        listchats.setAdapter(chatsAdapter);


        //method to get the data from the chats into the ui, populate the chats ui

        mDatabase = FirebaseDatabase.getInstance().getReference("messages");
        mDatabase.child(username).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    //used the map at first, idea wa sto get the username in a proper way but it was crappy

                   String map = (String) dataSnapshot.getKey();

                    arrayList.add(map);
                    Log.e("CHATS Map", "onDataChange: "+map);

                }
                chatsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}