package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.fuse2.databinding.ActivityMatchesBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Matches extends DrawerBase {
    ActivityMatchesBinding activityMatchesBinding; // nothing on top here

    //recycle view in the ui
    RecyclerView listMatches;

    //array to file with matches from db
    ArrayList arrayList;

    //recycle view adapter
    MatchesAdapter matchesAdapter;

    //database
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);

        activityMatchesBinding = ActivityMatchesBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Matches");

        setContentView(activityMatchesBinding.getRoot());

        //database

        mDatabase = FirebaseDatabase.getInstance().getReference("matches");


        getData();

    }

    public void getData(){
        //ui
        listMatches = findViewById(R.id.listMatches);

        listMatches.setHasFixedSize(true);
        listMatches.setLayoutManager(new LinearLayoutManager(this));

        arrayList = new ArrayList<>();
        matchesAdapter = new MatchesAdapter(this, arrayList);
        listMatches.setAdapter(matchesAdapter);

        mDatabase.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserDetails usr = dataSnapshot.getValue(UserDetails.class);



                    arrayList.add(usr);
                    //adding user with location gh only
                    // if(usr.getLocation().equalsIgnoreCase("gh")){

                    //}

                }
                matchesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}