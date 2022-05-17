package com.example.fuse2;

import android.content.Context;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReservesAdapter extends RecyclerView.Adapter<ReservesAdapter.RealMatchesViewHolder>{


    Context context;
    ArrayList<UserDetails> list;

    public ReservesAdapter(Context context, ArrayList<UserDetails> list) {
        this.context = context;
        this.list = list;
    }



    @NonNull
    @Override
    public ReservesAdapter.RealMatchesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.itemrealmatches, parent, false);
        return new RealMatchesViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ReservesAdapter.RealMatchesViewHolder holder, int position) {


        UserDetails usr = list.get(position);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("myReserve");


        //method to remove the match with the remove button
        holder.removeReserveBtn.setOnClickListener(new View.OnClickListener() {
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

        holder.reservenames.setText(usr.getName()+" "+usr.getSurname());
        holder.reserveAge.setText(usr.getBirthdate());
        holder.reserveLocation.setText(usr.getLocation());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RealMatchesViewHolder extends RecyclerView.ViewHolder {

        TextView reserveLocation, reserveAge, reservenames;
        ImageView userdp;
        Button removeReserveBtn;

        public RealMatchesViewHolder(@NonNull View itemView) {
            super(itemView);

            //matchView
            reserveAge = itemView.findViewById(R.id.reserveAge1);
            reserveLocation = itemView.findViewById(R.id.reserveLocation1);
            reservenames = itemView.findViewById(R.id.reservenames1);

            removeReserveBtn = itemView.findViewById(R.id.removeReserveBtn);
        }
    }
}
