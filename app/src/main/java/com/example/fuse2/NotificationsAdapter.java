package com.example.fuse2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ReserveViewHolder> {

    Context context;
    ArrayList<HashMap> list;



    //constructor

    public NotificationsAdapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ReserveViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.itemreserves, parent, false);
        return new ReserveViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ReserveViewHolder holder, int position) {


        Map map = list.get(position);


        //method to remove the match with the remove butto


        //method to add to chats
        holder.add_to_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference detailsRef = FirebaseDatabase.getInstance().getReference("userDetails");

                detailsRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String, String> me =(HashMap<String, String>) snapshot.getValue();


                        Log.e("USER DEATILS", "onDataChange: "+ me.get("name")+""+me.get("surname") );

                        //if the user profile is not null we can try to add the message thing to the db

                        if(me != null && map != null){
                            try {

                                addToChat(me.get("name")+""+me.get("surname"), map);

                            }
                            catch(Exception e){
                                Log.e("Getting username", "onDataChange: "+e.getMessage()+" " + map );
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

        holder.reservenames.setText(map.get("name")+" "+map.get("surname"));
        holder.reserveAge.setText(map.get("email").toString());
        holder.reserveLocation.setText(map.get("location").toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ReserveViewHolder extends RecyclerView.ViewHolder{

        TextView reserveLocation, reserveAge, reservenames;
        ImageView userdp;
        Button removeMatchBtn, add_to_chat;

        public ReserveViewHolder(@NonNull View itemView) {
            super(itemView);

            //matchView
            reserveAge = itemView.findViewById(R.id.reserveAge);
            reserveLocation = itemView.findViewById(R.id.reserveLocation);
            reservenames = itemView.findViewById(R.id.reservenames);
            add_to_chat = itemView.findViewById(R.id.add_to_chat);
            removeMatchBtn = itemView.findViewById(R.id.removeMatchBtn1);
        }


    }

    public void addToChat(String username, Map map){

        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");


        messagesRef.child(username).child(map.get("name")+""+map.get("surname")).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "Added "+map.get("name"), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "Failed to add "+map.get("name"), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
