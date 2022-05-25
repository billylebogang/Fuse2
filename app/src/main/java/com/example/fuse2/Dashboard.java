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


    TextView myName, pref_age, pref_location, pref_gender;

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

        myName = findViewById(R.id.namehere);
        pref_age =findViewById(R.id.pref_age);
        pref_location = findViewById(R.id.pref_location);
        pref_gender = findViewById(R.id.pref_gender);


        //bind to the drawer
        activityDashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        allocateActivityTitle("Dashboard");
        setContentView(activityDashboardBinding.getRoot());

        // getting users to display for match

        getUserData(); //getting user preferences

        getProfile(); //getting user data to display


        DBHelper dbHelper = new DBHelper();
        dbHelper.getMyProfile();

       // profileName.setText(dbHelper.getMyprofile()[0].getName() );



    }



    public void getData(String location, String gender, String to, String from){
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


                    //adding user with location gh only
                    if(usr.getLocation().equalsIgnoreCase(location)) {
                        if (gender.equalsIgnoreCase("women") && usr.getGender().equalsIgnoreCase("female")) {
                            theList.add(usr);
                        }
                        else if(gender.equalsIgnoreCase("men") && usr.getGender().equalsIgnoreCase("male")){
                            theList.add(usr);
                        }
                        else if (gender.equalsIgnoreCase("all")){
                            theList.add(usr);
                        }
                    }

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

                String myname = usr.get("name")+" "+usr.get("surname");
                myName.setText(myname);


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

                    pref_age.setText(map.get("ageFrom"));
                    pref_location.setText(map.get("location"));
                    pref_gender.setText(map.get("gender"));


                    Log.e("AGE FROM", "onComplete: "+ map.get("ageFrom"));

                    Log.e("LOCATION", "onComplete: "+ map.get("location"));

                    Log.e("GENDER", "onComplete: "+ map.get("gender"));

                    getData(map.get("location"),map.get("gender"), "20", "25");

                }
                else{
                    Log.e("MAP", "IT HAS FAILED");
                }
            }
        });



    }


}