package com.raveena.snug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UploadingActivity extends AppCompatActivity {
    Button uploadBtn;
    VideoView videoField;
    Spinner spinner;
    List<String> categoryNames;
    String videoType;
    private Uri videoUri;
    private static final int REQUEST_CODE = 1;
    private StorageReference videoRef;
    private DatabaseReference reference;
    private StorageTask uploadTask;
    private String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);

        uploadBtn = findViewById(R.id.uploadBtn);
        videoField = findViewById(R.id.videoview);
        spinner = findViewById(R.id.categories);
        categoryNames = new ArrayList<>();
        addCategoryNames();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplication(),
                android.R.layout.simple_spinner_item, categoryNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        // clicking this button will allow you to choose a video
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);

                videoRef = FirebaseStorage.getInstance().getReference("uploads");
                System.out.println(videoRef.getPath());
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                videoType = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            videoUri = data.getData();
            try{
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(UploadingActivity.this, "Upload in progress...", Toast.LENGTH_SHORT).show();
                } else {
                    UploadVideo();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = UploadingActivity.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //upload video filepath to firebase storage
    private void UploadVideo() {
        if (videoUri != null) {
            videoId = UUID.randomUUID().toString();
            final StorageReference fileReference = videoRef.child(videoId +
                    "." + getFileExtension(videoUri));
            uploadTask = fileReference.putFile(videoUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String videoUri2 = downloadUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("Videos").child(videoId);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("videoId", videoId);
                        hashMap.put("videoUri", videoUri2);
                        hashMap.put("filePath", videoId + "." + getFileExtension(videoUri));
                        hashMap.put("category", videoType);
                        reference.setValue(hashMap);
                        Toast.makeText(UploadingActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UploadingActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(UploadingActivity.this, "No Video Selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCategoryNames() {
        //categoryNames.add("Pick a Situation");
        categoryNames.add("Car");
        categoryNames.add("Party");
        categoryNames.add("Group");
        categoryNames.add("Walking");
        categoryNames.add("Alone");
        categoryNames.add("Work");
        categoryNames.add("School");
        categoryNames.add("Store");
        categoryNames.add("Home");
    }
}
