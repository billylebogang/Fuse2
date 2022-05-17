package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.fuse2.databinding.ActivityReservesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Reserves extends DrawerBase {

    ActivityReservesBinding activityReservesBinding;

    DatabaseReference resserveRef;

     ReservesAdapter reservesAdapter;

    ArrayList reservelist;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserves);

        activityReservesBinding = ActivityReservesBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Reserves");

        setContentView(activityReservesBinding.getRoot());


        resserveRef = FirebaseDatabase.getInstance().getReference("myReserve");

        Log.e("TAG", "onCreate: reserves on get data " );
        getReserves();

    }

    private void getReserves() {


        recyclerView = findViewById(R.id.reservesView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        reservelist = new ArrayList<>();

        reservesAdapter = new ReservesAdapter(this, reservelist);
        recyclerView.setAdapter(reservesAdapter);

        Log.e("TAG", "onCreate: inside on get data " );

        resserveRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    UserDetails usr = dataSnapshot.getValue(UserDetails.class);


                    Log.e("reserves", "onDataChange: "+ usr );

                    reservelist.add(usr);
                    //adding user with location gh only
                    // if(usr.getLocation().equalsIgnoreCase("gh")){

                    //}

                }
                reservesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


    }
}