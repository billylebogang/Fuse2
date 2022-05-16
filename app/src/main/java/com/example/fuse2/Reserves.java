package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;

public class Reserves extends DrawerBase {

    ActivityReservesBinding activityReservesBinding;

    DatabaseReference resserveRef;

    ReserveAdapter reserveAdapter;

    ArrayList<HashMap> reservelist;

    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserves);

        activityReservesBinding = ActivityReservesBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Reserves");

        setContentView(activityReservesBinding.getRoot());

        resserveRef = FirebaseDatabase.getInstance().getReference("Reserves");

        getData();

    }

    private void getData() {


        recyclerView = findViewById(R.id.reservesView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        reservelist = new ArrayList<>();
        reserveAdapter = new ReserveAdapter(this, reservelist);
        recyclerView.setAdapter(reserveAdapter);


        resserveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for( DataSnapshot dataSnapshot : snapshot.getChildren() ) {

                    HashMap<String, String> map = (HashMap<String, String>) dataSnapshot.getValue();

                    if(map.get("reserveEmail").equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                        reservelist.add(map);
                    }



                    Log.e("RESERVES", "onDataChange: " + dataSnapshot.getValue());
                }
                reserveAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("RESERVE", "onCancelled: "+ error.getMessage() );

            }
        });
    }
}