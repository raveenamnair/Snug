package com.raveena.snug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.raveena.snug.Model.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {

    VideoView video;
    String videoPath;
    Button refreshBtn;
    String videoCategory;
    ArrayList <String> l;
    boolean clicked = false;
    DatabaseReference reference;
    List<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        video = findViewById(R.id.videoView);
        refreshBtn = findViewById(R.id.RefreshBtn);

        // This will allow us to know which situation type was picked so we can display proper videos
        Bundle extra = getIntent().getExtras();
        videoCategory = null;
        if (extra != null) {
            videoCategory = extra.getString("SITUATION_TYPE");
        }

        // For debugging purposes
        //Toast.makeText(getApplicationContext(), videoCategory, Toast.LENGTH_SHORT).show();

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Videos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    Video video = snapshot1.getValue(Video.class);
                    videoPath = video.getFilePath();
                    if (video.getCategory() != null && video.getCategory().equals(videoCategory)) {
                        videoList.add(video);
                    }
                }
                Collections.shuffle(videoList);
                videoPath = videoList.get(0).getFilePath();
                StorageReference storageReference = storage.getReference().child("uploads/" + videoPath);
                System.out.println("VIDEOPATH " + videoPath);
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Got the download URL for 'users/me/profile.png'
                        System.out.println(uri);
                        video.setVideoURI(uri);
                        video.start();
                        //video.pause();
                        Toast.makeText(getApplicationContext(), "Video success", Toast.LENGTH_LONG).show();
                        System.out.println("REAL LIST");
                        for (Video v: videoList) {
                            System.out.println(v.getCategory());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Clicked refresh", Toast.LENGTH_SHORT).show();
            }
        });



//        StorageReference storageReference = null;
//        List<Video> specialList = videoList;
//        for (int i = 0; i < specialList.size(); i++) {
//            storageReference = storage.getReference().child("uploads/" + specialList.get(i).getFilePath());
//            System.out.println("VIDEOPATH " + specialList.get(i).getFilePath());
//            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    // Got the download URL for 'users/me/profile.png'
//                    System.out.println(uri);
//                    video.setVideoURI(uri);
//                    video.start();
//                    Toast.makeText(getApplicationContext(), "Video success", Toast.LENGTH_LONG).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle any errors
//                    Toast.makeText(getApplicationContext(), "Sorry, your requested video couldn't load", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            refreshBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    video.stopPlayback();
//                    clicked = true;
//                }
//            });
//            if (!clicked) {
//                video.stopPlayback();
//                break;
//            }
//
//        }


//        StorageReference storageReference = storage.getReference().child("uploads/" + videoPath);
//        System.out.println("VIDEOPATH " + videoPath);
//        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//            @Override
//            public void onSuccess(Uri uri) {
//                // Got the download URL for 'users/me/profile.png'
//                System.out.println(uri);
//                video.setVideoURI(uri);
//                video.start();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
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

    public List<Video> getSpecializedList() {
        List<Video> specialList = new ArrayList<>();
        for (int i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).getCategory().equals(videoCategory)) {
                specialList.add(videoList.get(i));
            }
        }
        Collections.shuffle(specialList);
        return specialList;
    }


}
