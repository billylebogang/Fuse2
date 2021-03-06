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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    ProgressBar loading;

    EditText username, password;
    Button loginBtn, loginRegisterBtn,resetpassword;
    FirebaseAuth mAuth;
    TextView linkSignUp, linkForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        mAuth = FirebaseAuth.getInstance();

        loginBtn = (Button) findViewById(R.id.loginBtn);
        //loginRegisterBtn = (Button) findViewById(R.id.loginRegisterBtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        //resetpassword = (Button)findViewById(R.id.forgotPasswordBtn);
        loading = findViewById(R.id.loading);
        linkSignUp = findViewById(R.id.link_signup);
        linkForgot = findViewById(R.id.forgotPassword);

        //forgot password
        linkForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    username.setError("User email required");
                    return;
                }                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Email code sent, to reset password", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Login.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Changing activity", "onClick: navigating to register screen");
                Intent intent = new Intent(Login.this, CreateAccount.class);
                startActivity(intent);
            }
        });


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = username.getText().toString().trim();
                String pass = password.getText().toString().trim();

                if(TextUtils.isEmpty(userName)){
                    username.setError("Username is required");
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    password.setError("Password is required");

                    return;

                }
                else{

                    loading.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(userName,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                loading.setVisibility(View.INVISIBLE);

                                Toast.makeText(Login.this, "Login...", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Login.this, Dashboard.class));
                                finish();
                            }
                            else{
                                Toast.makeText(Login.this, "Error "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
            }
        });
    }
}