package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class Register extends AppCompatActivity {


    //FROM UI
    private EditText  password, password2;
    private Button registerBtn , registerLoginBtn;
    private ProgressBar progressBar;

    //still ui staff
    private EditText registerName,registerSurname, registerEmail,registerBirthdate, registerLocation;
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
        setContentView(R.layout.activity_register);


        //initializing the fields
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        progressBar = findViewById(R.id.loading);
        registerBtn = findViewById(R.id.registerBtn);
        registerLoginBtn = findViewById(R.id.registerLoginBtn);

        registerName = findViewById(R.id.registerName);
        registerSurname = findViewById(R.id.registerSurname);
        registerEmail = findViewById(R.id.registerEmail);
        registerBirthdate = findViewById(R.id.registerBirthdate);
        registerLocation = findViewById(R.id.RegisterLocation);
        selectDate = findViewById(R.id.selectDate);
        errorBar = findViewById(R.id.errorBar);

        registerGenderGroup = findViewById(R.id.registerGenderGroup);







        //database stuff
        mAuth = FirebaseAuth.getInstance();
        USER  = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("userDetails");



        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePick();
            }
        });

        //on click to choose an image





        //TODO: USE FULL CODE BELOW

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createAccount();


            }
        });


        registerLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Register.this, Login.class));

            }
        });

    }


    //radio button checked



    public UserDetails createUserDetails(){
        //getting input data from the input fields
        String name = registerName.getText().toString().trim();
        String surname = registerSurname.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String birthday = registerBirthdate.getText().toString().trim();
        String location = registerLocation.getText().toString().trim();


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
        else if(TextUtils.isEmpty(location)){
            registerLocation.setError("Location is required");
        }
        else if(TextUtils.isEmpty(birthday)){
            registerBirthdate.setError("Birthdate is required");
        }
        else{

            //creating a user profile from data captured
            usrD = new UserDetails(name,surname,gender,email,location,birthday);


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

    public void datePick(){

        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog dpd = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                month = month+1;
                String date = day+"/"+month+"/"+year;
                registerBirthdate.setText( date);

            }
        }, year, month, day);
        dpd.show();
    }






    public void createAccount(){


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the input data
                String useremail = registerEmail.getText().toString().trim();
                String pass = password.getText().toString().trim();
                String pass2 = password2.getText().toString().trim();

                //validating the data inputs
                if(TextUtils.isEmpty(useremail)){
                    registerEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("Password is required");
                    return;
                }
                if(!(pass2.equals(pass)) ){
                    password2.setError("Password must match");
                    return;
                }

                else{
                    //TODO make a loader to work as the User is being registered


                    try {

                        mAuth.createUserWithEmailAndPassword(useremail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    String id = task.getResult().getUser().getUid();

                                    System.out.println(task.isSuccessful() + "  "+ id);

                                    //creating a db storage..
                                    if(id!=null){createProfile(id, createUserDetails());}
                                    else{
                                        Log.e("CREATING USER ID", "onComplete: id is null " );
                                    }

                                    Toast.makeText(Register.this, "Registering"+ id, Toast.LENGTH_SHORT).show();

                                }
                                else{
                                    Toast.makeText(Register.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }catch (Exception e){
                        Log.e("TRY CREATING USER", "onClick: "+ e.getMessage());
                    }

                    Log.e("REGISTER", "onClick: creating of the account" );
                }


            }
        });
    }


}