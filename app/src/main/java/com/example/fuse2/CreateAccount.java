package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateAccount extends AppCompatActivity {

    EditText accountEmail, accountPassword, accountPassword1;

    Button createAccBtn, loginInsteadBtn;


    //firebase stuff
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //UI
        accountEmail = findViewById(R.id.accountEmail);
        accountPassword = findViewById(R.id.accountPassword);
        accountPassword1 = findViewById(R.id.accountPassword1);
        //buttons

        createAccBtn = findViewById(R.id.createAccBtn);
        loginInsteadBtn = findViewById(R.id.loginInsteadBtn);

        createAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    createAccount();
            }
        });


        loginInsteadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(CreateAccount.this, Login.class));


            }
        });



    }

    public void createAccount(){

                //getting the input data
                String email = accountEmail.getText().toString().trim();
                String pass = accountPassword.getText().toString().trim();
                String pass2 = accountPassword1.getText().toString().trim();

                //validating the data inputs
                if(TextUtils.isEmpty(email)){
                    accountEmail.setError("Email is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    accountPassword.setError("Password is required");
                    return;
                }
                if(!(pass2.equals(pass)) ){
                    accountPassword1.setError("Password must match");
                    return;
                }

                else{
                    //TODO make a loader to work as the User is being registered


                    try {

                        Toast.makeText(CreateAccount.this, "Creating account", Toast.LENGTH_SHORT).show();


                        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){

                                    Toast.makeText(CreateAccount.this, "Account created", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(CreateAccount.this, Register.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(CreateAccount.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }catch (Exception e){
                        Log.e("TRY CREATING USER", "onClick: "+ e.getMessage());
                    }

                    Log.e("REGISTER", "onClick: creating of the account" );
                }
    }
}