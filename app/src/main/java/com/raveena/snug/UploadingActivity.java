package com.raveena.snug;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.core.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class UploadingActivity extends AppCompatActivity {

    Button uploadBtn;
    private Uri videoUri;
    private static final int REQUEST_CODE = 101;
    private StorageReference videoRef;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);

        uploadBtn = findViewById(R.id.uploadBtn);

        // clicking this button will allow you to choose a video
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

                // TODO Get the filepath of the video to upload to Firebase
                videoRef = FirebaseStorage.getInstance().getReference("uploads");

            }
        });
    }


//    //upload video filepath to firebase storage
//    private void UploadVideo() {
//        //TODO: progress bar?
//        if (videoUri != null) {
//            final StorageReference fileReference = videoRef.child();
//            uploadTask = fileReference.putFile()
//        }
//    }

}
