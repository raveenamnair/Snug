package com.raveena.snug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity {
    //widgets
    EditText nameET, passwordET, emailET, confirmPassET;
    Button signInBtn;

    //firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //initialize widgets
        nameET = findViewById(R.id.nameSignUp);
        passwordET = findViewById(R.id.passwordSignUp);
        emailET = findViewById(R.id.emailSignUp);
        confirmPassET = findViewById(R.id.confirmPassword);
        signInBtn = findViewById(R.id.signInBtn);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //adding event listener to sign in button
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}