package com.example.fuse2;

import android.content.Context;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchesViewHolder>{


    Context context;
    ArrayList<UserDetails> list;

    public MatchesAdapter(Context context, ArrayList<UserDetails> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public MatchesAdapter.MatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemmatches, parent, false);
        return new MatchesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MatchesAdapter.MatchesViewHolder holder, int position) {


        UserDetails usr = list.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("matches");


        //method to remove the match with the remove button
        holder.removeMatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(usr.getName()+" "+usr.getSurname()).removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Remove "+usr.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to remove"+usr.getName(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

        holder.matchnames.setText(usr.getName()+" "+usr.getSurname());
        holder.matchAge.setText(usr.getBirthdate());
        holder.matchLocation.setText(usr.getLocation());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MatchesViewHolder extends RecyclerView.ViewHolder {

        TextView  matchLocation, matchAge, matchnames;
        ImageView userdp;
        Button removeMatchBtn;

        public MatchesViewHolder(@NonNull View itemView) {
            super(itemView);

            //matchView
            matchAge = itemView.findViewById(R.id.matchAge);
            matchLocation = itemView.findViewById(R.id.matchLocation);
            matchnames = itemView.findViewById(R.id.matchnames);

            removeMatchBtn = itemView.findViewById(R.id.removeMatchBtn);
        }
    }
}
