package com.example.fuse2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FuseAdapter extends RecyclerView.Adapter<FuseAdapter.MyViewHolder> {


    Context context;
    ArrayList<UserDetails> list;

    public FuseAdapter(Context context, ArrayList<UserDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserDetails usr = list.get(position);
        //FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        holder.tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMatch(FirebaseAuth.getInstance().getCurrentUser().getUid(),usr);
                createReservee(usr.getEmail());
            }
        });



        holder.names.setText(usr.getName()+" "+usr.getSurname());
        holder.age.setText(usr.getBirthdate());
        holder.location.setText(usr.getLocation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView names, age, location, matchLocation, matchAge, matchnames;
        ImageView userdp;
        ImageButton tick,cross;
        Button removeMatchBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            names = itemView.findViewById(R.id.fuseNames);
            age = itemView.findViewById(R.id.fuseAge);
            location = itemView.findViewById(R.id.fuseLocation);

            userdp = itemView.findViewById(R.id.userdp);
            cross = itemView.findViewById(R.id.cross);
            tick = itemView.findViewById(R.id.tick);
            
            //matchView
            matchAge = itemView.findViewById(R.id.matchAge);
            matchLocation = itemView.findViewById(R.id.matchLocation);
            matchnames = itemView.findViewById(R.id.matchnames);
            
            removeMatchBtn = itemView.findViewById(R.id.removeMatchBtn);
            
        }
    }


    public void addMatch(String id, UserDetails usr){

        DatabaseReference matchesRef = FirebaseDatabase.getInstance().getReference("matches");


                matchesRef.child(id).child(usr.getName()+" "+usr.getSurname()).setValue(usr).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context, "Added to matches", Toast.LENGTH_SHORT).show();
                        }
                        else Toast.makeText(context, "Failed to add", Toast.LENGTH_SHORT).show();

                    }
                });


    }


    public void createNotification(UserDetails usr){

        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("notifications");

        String receiver = usr.getEmail();
        String sender = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String message =  "At 20:50 , 12/03/2020   you have a match request, you can accept or decline check reserves";

        Notification notification = new Notification(receiver, message, sender);


        notificationRef.child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).setValue(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("NOTIFICATION", "onSuccess: Notification added ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("NOTIFICATION", "onFail: "+e.getMessage());
            }
        });
    }

    public  void addToReserves(String name, String surname, String location, String email, String reserveEmail){

        DatabaseReference reservesRef = FirebaseDatabase.getInstance().getReference("Reserves");

        Map<String, String> map = new TreeMap<>();
        map.put("name", name);
        map.put("surname", surname);
        map.put("location", location);
        map.put("email", email);
        map.put("reserveEmail", reserveEmail);

        reservesRef.child(System.currentTimeMillis()+"").setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.e("NOTIFICATION", "onSuccess: Notification added ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("NOTIFICATION", "onFail: "+e.getMessage());
            }
        });



    }
    public void createReservee(String res){

        DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

        detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> usr =(HashMap<String, String>) snapshot.getValue();

                Log.e("USER DEATILS", "onDataChange: "+ usr.get("name") );

                addToReserves(usr.get("name"), usr.get("surname"), usr.get("location"), usr.get("email"), res);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
