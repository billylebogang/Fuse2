package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    ProgressBar loading;

    EditText username, password;
    Button loginBtn, loginRegisterBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mAuth = FirebaseAuth.getInstance();

        loginBtn = findViewById(R.id.loginBtn);
        loginRegisterBtn = findViewById(R.id.loginRegisterBtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        loading = findViewById(R.id.loading);


        loginRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, CreateAccount.class));

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