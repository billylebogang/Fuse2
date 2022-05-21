package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Messages2 extends AppCompatActivity {



   protected MessageAdapter messageAdapter;

   protected ArrayList arrayList;

    protected RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages2);


        //getting the friend/sender from the the chat activity
        String message_receiver = getIntent().getStringExtra("friend_name");


        //getting messages using mmy username and friend username

        DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

        detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, String> me =(HashMap<String, String>) snapshot.getValue();


                Log.e("MESSAGE get", "onDataChange: "+ me.get("name")+""+me.get("surname") );

                //if the user profile is not null we can try to add the message thing to the db

                if(me != null){
                    //tryin to send message if error it does nothing and logs it out
                    try {

                        getSentMessages(me.get("name")+""+me.get("surname"),message_receiver);

                    }
                    catch (Exception e){
                        Log.e("MESSAGE ERROR", ""+ e.getMessage() );
                    }
                    finally {
                        return;
                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("MESSAGE ERROR", ""+ error.getMessage() );
            }
        });
    }



    //methods


    private void getSentMessages(String me, String friend) {

        //method to get sent messages

        recyclerView = findViewById(R.id.messagesView2);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        arrayList = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, arrayList);
        recyclerView.setAdapter(messageAdapter);

        Log.e("TAG", "onCreate: inside on get message" );

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(me).child(friend).child("sent");

        messagesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    Message message = dataSnapshot.getValue(Message.class);


                    Log.e("MESSAGE", "onDataChange: "+ message.getContent() );

                    arrayList.add(message);


                }
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("getSentMessages", "onCancelled: "+error );

            }

        });


    }
}