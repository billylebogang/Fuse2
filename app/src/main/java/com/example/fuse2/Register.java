package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class Register extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

    DatePickerDialog date;
    //FROM UI
    private String selectedlocation;
    private EditText  password, password2;
    private Button registerBtn , registerLoginBtn;
    private ProgressBar progressBar;

    //still ui staff
    private EditText registerName,registerSurname, registerEmail,registerBirthdate, bio;
    private Spinner  registerLocation;
    private Button profileSetupNextBtn, selectDate;
    private RadioGroup registerGenderGroup;
    private RadioButton genderButton;
    //private ImageButton profileImage;
    private TextView errorBar;

    //database stuff
    private FirebaseAuth mAuth; FirebaseUser USER;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private StorageReference storageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        //initializing the fields
        progressBar = findViewById(R.id.loading);
        registerBtn = findViewById(R.id.registerBtn);
        //registerLoginBtn = findViewById(R.id.registerLoginBtn);
        registerName = findViewById(R.id.registerName);
        registerSurname = findViewById(R.id.registerSurname);
        registerEmail = findViewById(R.id.registerEmail);
        registerBirthdate = findViewById(R.id.registerBirthdate);
        bio = findViewById(R.id.bio);
        //selectDate = findViewById(R.id.selectDate);
        errorBar = findViewById(R.id.errorBar);

        registerGenderGroup = findViewById(R.id.registerGenderGroup);

        getlocation();

        //database stuff
        mAuth = FirebaseAuth.getInstance();
        USER  = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("userDetails");


        //setting the email
        registerEmail.setText(USER.getEmail());

        registerBirthdate.setInputType(InputType.TYPE_NULL);

        registerBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cal = Calendar.getInstance();
                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH);
                int year = cal.get(Calendar.YEAR);

                date = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        registerBirthdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
                date.show();
            }
        });


        //on click to choose an image


        //TODO: USE FULL CODE BELOW

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(USER != null){
                    createProfile(USER.getUid(),createUserDetails());
                }
                else{
                    Toast.makeText(Register.this, "Can not set profile without account", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*registerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Register.this, Login.class));

            }
        });*/

    }


    //radio button checked
    public UserDetails createUserDetails(){
        //getting input data from the input fields
        String name = registerName.getText().toString().trim();
        String surname = registerSurname.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String birthday = registerBirthdate.getText().toString().trim();
        String location = selectedlocation;
        String biography = bio.getText().toString().trim();


        //gender thing
        int radioId = registerGenderGroup.getCheckedRadioButtonId();

        genderButton = findViewById(radioId);

        String gender = genderButton.getText().toString();


        //object
        UserDetails usrD = null;

        //validating
        if(TextUtils.isEmpty(name)){
            registerName.setError("Name is required");
        }
        else if(TextUtils.isEmpty(surname)){
            registerSurname.setError("Surname is required");
        }
        else if(TextUtils.isEmpty(email)){
            registerEmail.setError("Email is required");
        }
        else if(TextUtils.isEmpty(location) || location.equalsIgnoreCase("Set location")){
            Toast.makeText(this, "Location is required", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(birthday)){
            registerBirthdate.setError("Birthdate is required");
        }
        else if(TextUtils.isEmpty(biography)){
            bio.setError("Add details about your self");
        }
        else{

            //creating a user profile from data captured
            usrD = new UserDetails(name,surname,gender,email,location,birthday,biography);


        }
        return usrD;
    }

    //method to create a user profile
    public void createProfile(String id,UserDetails ud){

      if(ud != null){
             mDatabase.child(id).setValue(ud).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    // starting activity
                    startActivity(new Intent(Register.this, Preferences.class));
                    finish();
                }
                else{
                    Toast.makeText(Register.this, "is not working", Toast.LENGTH_SHORT).show();
                }
            }
        });

      }else Log.e("Profile creation", "createProfile: user deatils are empty " );

    }

    public void getlocation(){

        registerLocation = findViewById(R.id.RegisterLocation);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.places
        , android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        registerLocation.setAdapter(adapter);
        registerLocation.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         selectedlocation = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}