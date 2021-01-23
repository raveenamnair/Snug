package com.raveena.snug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class VideoListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        // This will allow us to know which situation type was picked so we can display proper videos
        Bundle extra = getIntent().getExtras();
        String value = null;
        if (extra != null) {
            value = extra.getString("SITUATION_TYPE");
        }

        // For debugging purposes
        Toast.makeText(getApplicationContext(), value, Toast.LENGTH_SHORT).show();
    }
}
