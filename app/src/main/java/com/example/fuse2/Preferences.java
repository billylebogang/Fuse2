package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.TreeMap;

public class Preferences extends AppCompatActivity {

    protected EditText preferedLocation,fromAge, toAge, hobbies;
    protected RadioGroup preferedGenderRadioGroup;
    protected ImageButton hobbieBtn;
    protected Button prefNextBtn;

    //database stuff
    private FirebaseAuth mAuth; FirebaseUser USER;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        //UI instantiations
        preferedLocation = findViewById(R.id.preferedLocation);
        fromAge = findViewById(R.id.fromAge);
        toAge = findViewById(R.id.toAge);
        hobbies = findViewById(R.id.hobbies);
        preferedGenderRadioGroup = findViewById(R.id.preferedGenderRadioGroup);
        hobbieBtn = findViewById(R.id.imageButton);
        prefNextBtn = findViewById(R.id.prefNextBtn);


        //database stuff
        mAuth = FirebaseAuth.getInstance();
        USER  = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("preferences");




        prefNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //UI data capture
                String location = preferedLocation.getText().toString().trim();
                String age = fromAge.getText().toString().trim() +" - " + toAge.getText().toString().trim();
                String gender = "Female"; //((RadioButton)findViewById(preferedGenderRadioGroup.getCheckedRadioButtonId())).getText().toString();


                if (TextUtils.isEmpty(location)) {
                    preferedLocation.setError("Please set Location");
                    return;
                }
                if(TextUtils.isEmpty(age)){
                    toAge.setError("Please fill");
                    fromAge.setError("Please fill");
                    return;
                }
                if(TextUtils.isEmpty(gender)){
                    return;
                }
                else{

                    if(USER != null){
                        String id = USER.getUid();
                        setPrefs(id,location,age, gender);

                    }
                    else{
                        Toast.makeText(Preferences.this, "Could not load user", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
            }
        });


    }


    // TODO: CODE IS HER BELOW

    public void setPrefs(String userId, String location, String age,String gender){

        //declaring a tree map to store the dat before passing it to the db
        TreeMap<String , String> prefs = new TreeMap<>();
        prefs.put("location", location);
        prefs.put("age", age);
        prefs.put("gender", gender);

        mDatabase.child(userId).setValue(prefs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    System.out.println("pref has been set ..");
                    startActivity(new Intent(Preferences.this, Dashboard.class));

                }
                else{
                    Toast.makeText(Preferences.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}