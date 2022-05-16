package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fuse2.databinding.ActivityDashboardBinding;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends DrawerBase {



    //binding to the drawer
    ActivityDashboardBinding activityDashboardBinding;


    TextView profileName;

   // ImageButton profileImage;

    RecyclerView listUsers;

    //database
    private FirebaseAuth mAuth; FirebaseUser USER;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase,pDatabase, nDatabase;
    private StorageReference storageRef;

    //UI for adapters
    FuseAdapter fa;
    ArrayList<UserDetails> theList;


    //picture related uri


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //database
        mAuth = FirebaseAuth.getInstance();
        USER  = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("userDetails");
        pDatabase = database.getReference("preferences");
        nDatabase = database.getReference("notifications");
        storageRef = FirebaseStorage.getInstance().getReference();


        //picture thing
       // profileImage = findViewById(R.id.dpImage);

        profileName = findViewById(R.id.mynamehere);


        //bind to the drawer
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Dashboard");
        setContentView(activityDashboardBinding.getRoot());

        getData(); // getting users to display for match

        getUserData(); //getting user preferences

        getProfile(); //getting user data to display


        DBHelper dbHelper = new DBHelper();
        dbHelper.getMyProfile();

       // profileName.setText(dbHelper.getMyprofile()[0].getName() );



    }



    public void getData(){
        //ui
        listUsers = findViewById(R.id.listUsers);

        listUsers.setHasFixedSize(true);
        listUsers.setLayoutManager(new LinearLayoutManager(this));

        theList = new ArrayList<>();
        fa = new FuseAdapter(this, theList);
        listUsers.setAdapter(fa);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    UserDetails usr = dataSnapshot.getValue(UserDetails.class);



                    theList.add(usr);
                    //adding user with location gh only
                   // if(usr.getLocation().equalsIgnoreCase("gh")){

                    //}

                }
                fa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void getProfile(){

        mDatabase.child(USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, String> usr =(HashMap<String, String>) snapshot.getValue();

                Log.e("USER DEATILS", "onDataChange: "+ usr.get("name") );

                profileName.setText(usr.get("name"));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void getUserData(){ //getting user preferences

        pDatabase.child(USER.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful()){
                    HashMap<String, String> map = new HashMap();

                    map =(HashMap) task.getResult().getValue();

                    Log.e("MAP", "onComplete: "+ map);

                    Log.e("MAP", "onComplete: "+ task.getResult().getValue());

                }
                else{
                    Log.e("MAP", "IT HAS FAILED");
                }
            }
        });



    }


}