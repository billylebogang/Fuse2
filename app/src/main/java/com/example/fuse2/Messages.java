package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Messages extends AppCompatActivity {

    //UI variables
    private EditText input_message;
    private ImageButton send_message_btn;
    private Button see_sent;


    //adapter that connects to the UI item thing
    MessageAdapter messageAdapter;

    //arraylist to store the data is received from the db(messages)
    ArrayList arrayList;

    //the UI recycle view
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        //instantiating the variables
        input_message = findViewById(R.id.input_message);
        send_message_btn = findViewById(R.id.send_message_btn);
        see_sent = findViewById(R.id.see_sent);

        //intent to send the friend the be able to get messages use for that specific
        String message_receiver = getIntent().getStringExtra("friend_name");



        //to get to sent messages
        see_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //passing the intend with friend username to get specific user messages
                Intent intent = new Intent(getApplicationContext(), Messages2.class);
                intent.putExtra("friend_name",message_receiver );
                startActivity(intent);
            }
        });


        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //database reference to the data
                DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

                //getting the current user details
                detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        //getting the data into a map
                        Map<String, String> me =(HashMap<String, String>) snapshot.getValue();

                        //validation checking wat is brought back
                        Log.e("MESSAGE SEN", "onDataChange: "+ me.get("name")+""+me.get("surname") );

                        //if the user profile is not null we can try to add the message thing to the db

                        if(me != null){
                            //trying to send message ,if error it does nothing and logs it out
                            try {
                                sendMessage(createMessage(me.get("name")+""+me.get("surname"),message_receiver));
                            }
                            catch (Exception e){
                                Toast.makeText(Messages.this, ""+e.getMessage().trim(), Toast.LENGTH_SHORT).show();
                                Log.e("MESSAGE SENDING", "onClick: "+ e.getMessage() );
                            }
                            finally {
                                return;
                            }

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    //for database error to show in toast
                        Toast.makeText(Messages.this, ""+error.getMessage().trim(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });



        //getting messages
        //reference
        DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

        detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Map<String, String> me =(HashMap<String, String>) snapshot.getValue();


                Log.e("MESSAGE get", "onDataChange: "+ me.get("name")+""+me.get("surname") );

                //if the user profile is not null we can try to get the message thing to the db

                if(me != null){
                    //trying to get received message, if error it does nothing and logs it out
                    try {

                        //calling the function
                        getReceivedMessages(me.get("name")+""+me.get("surname"),message_receiver);
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

                Toast.makeText(Messages.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private String getDateTime(){

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        return dateFormat.format(calendar.getTime());
    }

    protected Message createMessage(String sender,String receiver){

        //method to create a message from the input, using the message object
        String content = input_message.getText().toString().trim();
        //message object
        Message message = new Message();
        if (TextUtils.isEmpty(content)){
            input_message.setError("say something");
        }
        else{
            message.setContent(content);
            message.setDate(getDateTime());
            message.setReceiver(receiver);
            message.setSender(sender);
            message.setTime("");

        }
      return message;
    }

    public void sendMessage(Message message){

        //method to send the message to the database

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        //add message  on the receiver as the received
        messagesRef.child(message.getReceiver()).child(message.getSender()).child("received").child(System.currentTimeMillis()+"").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(Messages.this, "message sent!", Toast.LENGTH_SHORT).show();
                input_message.setText("");
                input_message.requestFocus();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Messages.this, "Message not sent, check network", Toast.LENGTH_SHORT).show();
                Log.e("MESSAGE FAIL", "onFailure: "+e.getMessage());
            }
        });

        //adding the message on the sender side as sent messages
        messagesRef.child(message.getSender()).child(message.getReceiver()).child("sent").child(System.currentTimeMillis()+"").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                Toast.makeText(Messages.this, "message sent!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Messages.this, "Message not sent, check network", Toast.LENGTH_SHORT).show();
                Log.e("MESSAGE FAIL", "onFailure: "+e.getMessage());
            }
        });


    }


    private void getReceivedMessages(String me, String friend) {

        //getting received messages into the recycle view

        recyclerView = findViewById(R.id.messagesView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        arrayList = new ArrayList<>();

        messageAdapter = new MessageAdapter(this, arrayList);
        recyclerView.setAdapter(messageAdapter);


        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages").child(me).child(friend).child("received");

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

                Toast.makeText(Messages.this, "Could not get messages", Toast.LENGTH_SHORT).show();
                Log.e("getRecieMessages", "onCancelled: "+error );
            }

        });


    }


}