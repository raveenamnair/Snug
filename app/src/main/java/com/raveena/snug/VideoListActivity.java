package com.raveena.snug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class VideoListActivity extends AppCompatActivity {

    VideoView video;
    ArrayList <String> l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        video = findViewById(R.id.videoView);

        // This will allow us to know which situation type was picked so we can display proper videos
        Bundle extra = getIntent().getExtras();
        String value = null;
        if (extra != null) {
            value = extra.getString("SITUATION_TYPE");
        }

        // For debugging purposes
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        StorageReference storageReference = storage.getReference().child("uploads/1611424481054.mp4");
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                System.out.println(uri);
                video.setVideoURI(uri);
                video.start();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
//        l = new ArrayList<>();
//        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
//            @Override
//            public void onSuccess(ListResult listResult) {
//                for (StorageReference fileRef : listResult.getItems()) {
//                    l.add(fileRef.getPath());
//                    System.out.println(fileRef.getPath());
//                }
//                StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("/uploads/1611424481054.mp4");
//                Uri file = Uri.fromFile(new File("path/to/folderName/file.jpg"));
//                UploadTask uploadTask = storageRef.putFile(file);
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
//            }
//        });
//
//        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("uploads/1611424481054.mp4");
//        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                video.setVideoURI(uri);
//                video.start();
//            }
//        });

        //video.start();


    }


}
