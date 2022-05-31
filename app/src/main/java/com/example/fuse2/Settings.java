package com.example.fuse2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fuse2.databinding.ActivitySettingsBinding;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Settings extends DrawerBase {

    ActivitySettingsBinding activitySettingsBinding;

    ActivitySettingsBinding binding;

    TextView settingsError, name,detailsSurname,detailsDateOfBirth, detailsEmail, detailsLocation,detailsBio;
    ImageView dp;
    Button signoutBtn, addDp, updateBtn, update_prefs;

    //database
    private FirebaseAuth mAuth; FirebaseUser USER;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase,pDatabase;

    private StorageReference storageRef;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_settings);

        activitySettingsBinding = ActivitySettingsBinding.inflate(getLayoutInflater());

        allocateActivityTitle("Settings");

        setContentView(activitySettingsBinding.getRoot());

        //database settings

        mAuth = FirebaseAuth.getInstance();
        USER  = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference("userDetails");
        pDatabase = database.getReference("preferences");
        storageRef = FirebaseStorage.getInstance().getReference("profilePictures/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/");


        //ui
        updateBtn = findViewById(R.id.update_profile);
        update_prefs = findViewById(R.id.update_prefs);
        dp = findViewById(R.id.dp);
        name = findViewById(R.id.deailsName);
        detailsSurname = findViewById(R.id.detailsSurname);
        detailsDateOfBirth = findViewById(R.id.detailsDateOfBirth);
        detailsEmail = findViewById(R.id.detailsEmail);
        detailsLocation = findViewById(R.id.detailsLocation);
        detailsBio = findViewById(R.id.detailsBio);
        settingsError = findViewById(R.id.errorBar1);
        signoutBtn =findViewById(R.id.signoutBtn);

        addDp = findViewById(R.id.addDp);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), updateProfile.class));
            }
        });

        update_prefs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this, Preferences.class));
            }
        });

        signoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Settings.this, Login.class));
                finish();
            }
        });
        addDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        getUserData();

        //getting the profile image into the image view
        Glide.with(Settings.this).load(storageRef.child("profilePic.jpg").getDownloadUrl()).into(dp);

        mDatabase.child(USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDetails profile = snapshot.getValue(UserDetails.class);

                if(profile != null){
                    name.setText(profile.getName());
                    detailsSurname.setText(profile.getSurname());
                    detailsDateOfBirth.setText(profile.getBirthdate());
                    detailsEmail.setText(profile.getEmail());
                    detailsLocation.setText(profile.getLocation());
                    detailsBio.setText("I am a nice guy by nature, i want lengwanda");


                    Toast.makeText(Settings.this, " "+ profile.getEmail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


                Toast.makeText(Settings.this, "it has failed to  ", Toast.LENGTH_SHORT).show();
            }
        });

   /* public void getProfile(){

        mDatabase.child(USER.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map usr =(HashMap) snapshot.getValue();

                Log.e("USER DEATILS", "onDataChange: "+ snapshot.getValue() );

                if(usr != null){
                    name.setText(usr.get("name").toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/



        //CREATING AN IMAGE
        try {
            File localfile = File.createTempFile("tempfile", ".jpg");

            storageRef.child("profilePic.jpg").getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());

                            settingsError.setText("it has success");
                            binding.dp.setImageBitmap(bitmap);
                        }
                    }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull FileDownloadTask.TaskSnapshot snapshot) {
                    settingsError.setText("getting dp");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    settingsError.setText(""+e.getMessage());
                }
            });
        }
        catch (IOException e){
            e.printStackTrace();
            settingsError.setText(e.getMessage());
        }


    }

    public void choosePicture(){

        Intent intent = new Intent();

        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent,2);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2 && /*requestCode == RESULT_OK &&*/ data != null){

            imageUri = data.getData();
            System.out.println("I HAVE RUN RECEIVED DATA");
            upload();

        }

        else{
            System.out.println("RESULT ARE BAD");

            return;

        }    }



    //TODO: CODE HERE

    public void upload(){

        //get string extension
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String ext = mime.getExtensionFromMimeType(getContentResolver().getType(imageUri));

        if (imageUri == null){
            Toast.makeText(this, "Select image", Toast.LENGTH_SHORT).show();
        }
        else{

            //will uploading it will listen to success, failure, progress
            storageRef.child("profilePic."+ext).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Settings.this, "Picture uploaded succesfully", Toast.LENGTH_SHORT).show();

                    dp.setImageURI(imageUri);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Settings.this, "Picture uploaded failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    settingsError.setText("Picture uploading...");
                }
            });

        }

    }


    protected void getUserData(){ //getting user preferences

        pDatabase.child(USER.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                if(task.isSuccessful()){
                    HashMap<String, String> map = new HashMap();

                    map =(HashMap) task.getResult().getValue();

                    if(map != null){
                        String pref_age = (map.get("ageFrom"));
                        String pref_age2 = (map.get("ageTo"));
                         String pref_location = (map.get("location"));
                        String pref_gender=(map.get("gender"));

                        detailsBio.setText("I prefer "+pref_gender+" from "+ pref_location+" aged from "+ pref_age+" to "+pref_age2+"." );
                    }

                }
                else{
                    Log.e("MAP", "IT HAS FAILED");
                }
            }
        });



    }

}