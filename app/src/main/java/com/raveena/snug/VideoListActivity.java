package com.raveena.snug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    int counter = 0;
    DatabaseReference reference;
    List<Video> videoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        video = findViewById(R.id.videoView);
        refreshBtn = findViewById(R.id.RefreshBtn);
        ImageButton play = findViewById(R.id.playbtn);
//        play.setBackgroundResource(R.drawable.animation_list);
//        final AnimationDrawable playAnimation = (AnimationDrawable) play.getBackground();

        // This will allow us to know which situation type was picked so we can display proper videos
        Bundle extra = getIntent().getExtras();
        videoCategory = null;
        if (extra != null) {
            videoCategory = extra.getString("SITUATION_TYPE");
        }
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video.isPlaying()) {
                    Toast.makeText(getApplicationContext(), "clicked", Toast.LENGTH_SHORT).show();
                    video.pause();
                } else {
                    video.start();
                }
            }
        });
//        video.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent event) {
//                if (video.isPlaying()) {
//                    video.pause();
//                }
//                else {
//                    video.start();
//                }
//                return true;
//            }
//        });

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
                generateVideo(storage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Page Refreshed", Toast.LENGTH_SHORT).show();
                generateVideo(storage);
            }
        });
    }

    private void generateVideo(FirebaseStorage storage) {
        if (counter >= videoList.size()) {
            counter = 0;
        }
        videoPath = videoList.get(counter).getFilePath();
        counter++;
        StorageReference storageReference = storage.getReference().child("uploads/" + videoPath);
        System.out.println("VIDEOPATH " + videoPath);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'
                System.out.println(uri);
                video.setVideoURI(uri);
                //video.start();
                video.seekTo(1);
                video.pause();
                for (Video v: videoList) {
                    System.out.println(v.getCategory());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getApplicationContext(), "Video Failed", Toast.LENGTH_LONG).show();
            }
        });
    }
}
