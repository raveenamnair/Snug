package com.raveena.snug;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * CategoriesActivity class
 *
 * This class is the main "Dashboard" for the app. It lists the various situations users can
 * choose from for their video option. Once clicking on a specific category, it takes them to another
 * page that has the list of videos for that scenario. If the user wants to upload a video, then they
 * can click the plus button at the bottom right hand corner which will verify if they are logged in
 * and proceed from there for a video creation.
 *
 * If a specific scenario is chosen, a String is sent to the next activity with the name of the
 * situation specified
 */
public class CategoriesActivity extends AppCompatActivity implements View.OnClickListener {

    CardView car_card, walk_card, party_card, group_card, alone_card, work_card, school_card,
            store_card, home_card;
    ImageButton add_fab;

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        // Hooking everything up with their id #
        car_card = findViewById(R.id.car_card);
        party_card = findViewById(R.id.party_card);
        group_card = findViewById(R.id.group_card);
        walk_card = findViewById(R.id.walk_card);
        alone_card = findViewById(R.id.alone_card);
        work_card = findViewById(R.id.work_card);
        school_card = findViewById(R.id.school_card);
        store_card = findViewById(R.id.store_card);
        home_card = findViewById(R.id.home_card);
        add_fab = findViewById(R.id.addFAB);

        car_card.setOnClickListener(this);
        party_card.setOnClickListener(this);
        group_card.setOnClickListener(this);
        walk_card.setOnClickListener(this);
        alone_card.setOnClickListener(this);
        work_card.setOnClickListener(this);
        school_card.setOnClickListener(this);
        store_card.setOnClickListener(this);
        home_card.setOnClickListener(this);
        add_fab.setOnClickListener(this);

        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void onClick(View v) {
        Intent goToNextActivity = null;
        if (v.getId() == R.id.addFAB) {
            if (firebaseUser != null) {
                goToNextActivity = new Intent(getApplicationContext(), UploadingActivity.class);
            } else {
                goToNextActivity = new Intent(getApplicationContext(), LoginActivity.class);
            }
        } else {
            String situationType = "";
            switch (v.getId()) {
                case R.id.car_card:
                    situationType = "Car";
                    break;
                case R.id.party_card:
                    situationType = "Party";
                    break;
                case R.id.group_card:
                    situationType = "Group";
                    break;
                case R.id.walk_card:
                    situationType = "Walk";
                    break;
                case R.id.alone_card:
                    situationType = "Alone";
                    break;
                case R.id.work_card:
                    situationType = "Work";
                    break;
                case R.id.school_card:
                    situationType = "School";
                    break;
                case R.id.store_card:
                    situationType = "Store";
                    break;
                case R.id.home_card:
                    situationType = "Home";
                    break;
            }
            goToNextActivity = new Intent(getApplicationContext(), VideoListActivity.class);
            // this tells what type of video to have listed
            goToNextActivity.putExtra("SITUATION_TYPE", situationType);

        }
        startActivity(goToNextActivity);
    }
}
