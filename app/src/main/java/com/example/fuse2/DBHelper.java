package com.example.fuse2;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.TreeMap;

public class DBHelper {

    private FirebaseAuth mAuth; FirebaseUser USER;
    private FirebaseDatabase database;
    private DatabaseReference profileRef;
    private StorageReference storageRef;

    private UserDetails[] myprofile;


    public DBHelper(){

        mAuth = FirebaseAuth.getInstance();
        USER  = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        profileRef = database.getReference("userDetails");
    }


   /* public boolean addToMatches(String id, TreeMap map ){

        boolean flag = false;

        matchesRef.child(id).setValue(map)

        return  flag;
    }*/



    public void getMyProfile() {

        profileRef.child(USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {

            UserDetails userDetails;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null) {
                    userDetails = snapshot.getValue(UserDetails.class);


                } else {
                    Log.e("GET MY PROFILE", "onDataChange:" + snapshot.getValue());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("GET MY PROFILE", "onCancelled: " + error.getMessage());
            }
        });
    }

    public UserDetails getProfile( UserDetails userDetails){
        return userDetails;
    }


    public UserDetails[] getMyprofile() {
        return myprofile;
    }
}
