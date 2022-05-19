package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Messages extends AppCompatActivity {

    //UI variables
    private EditText input_message;
    private ImageButton send_message_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        input_message = findViewById(R.id.input_message);
        send_message_btn = findViewById(R.id.send_message_btn);

        //intent
        String message_receiver = getIntent().getStringExtra("friend_name");


        send_message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

                detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String, String> me =(HashMap<String, String>) snapshot.getValue();


                        Log.e("MESSAGE SEN", "onDataChange: "+ me.get("name")+""+me.get("surname") );

                        //if the user profile is not null we can try to add the message thing to the db

                        if(me != null){
                            //tryin to send message if error it does nothing and logs it out
                            try {
                                sendMessage(createMessage(me.get("name")+""+me.get("surname"),message_receiver));
                            }
                            catch (Exception e){
                                Log.e("MESSAGE SENDING", "onClick: "+ e.getMessage() );
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
        });

    }

    private String getDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

    protected Message createMessage(String sender,String receiver){
        String content = input_message.getText().toString().trim();
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

        messagesRef.child(message.getReceiver()).child(message.getSender()).child("received").child(System.currentTimeMillis()+"").setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
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
}