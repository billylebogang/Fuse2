package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class updateProfile extends AppCompatActivity {

    private EditText newpassword, newpassword1;
    private Button submit;

    FirebaseUser USER;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_pass);


        newpassword = findViewById(R.id.newPassword);
        newpassword1 = findViewById(R.id.newPassword1);
        submit = findViewById(R.id.submit);

        USER = FirebaseAuth.getInstance().getCurrentUser();

        String pass = newpassword.getText().toString().trim();
        String pass2 = newpassword1.getText().toString().trim();

        if(TextUtils.isEmpty(pass)){
            newpassword.setError("password required");
            return;
        }
        if(!pass.equals(pass2)){
            newpassword.setError("password must match");
            return;
        }
        else{

            USER.updatePassword(pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(updateProfile.this, "Password changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(updateProfile.this, Settings.class));
                        finish();
                    }
                }
            });
        }


    }
}