package com.raveena.snug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    //widgets
    EditText nameET, passwordET, emailET, confirmPassET;
    Button signUpBtn, signInBtn;
    TextInputLayout emailTextTL, passwordTextTL, confirmPasswordTL, nameTL;

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
        signUpBtn = findViewById(R.id.signUpBtn2);
        signInBtn = findViewById(R.id.signInBtn);
        emailTextTL = findViewById(R.id.emailTextTL);
        passwordTextTL = findViewById(R.id.passwordTextTL);
        confirmPasswordTL = findViewById(R.id.confirmPasswordTL);
        nameTL = findViewById(R.id.nameTextTL);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();

        //go back to login activity
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

        //adding event listener to sign up button
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameText = nameET.getText().toString();
                String emailText = emailET.getText().toString();
                String passwordText = passwordET.getText().toString();
                String confirmPassText = confirmPassET.getText().toString();
                nameTL.setErrorEnabled(false);
                emailTextTL.setErrorEnabled(false);
                passwordTextTL.setErrorEnabled(false);
                confirmPasswordTL.setErrorEnabled(false);

                //make sure all fields are filled in
                if (TextUtils.isEmpty(nameText)) {
                    nameTL.setError("Please enter your full name");
                } else if (TextUtils.isEmpty(emailText)) {
                    emailTextTL.setError("Please enter your email address");
                } else if (TextUtils.isEmpty(passwordText)) {
                    passwordTextTL.setError("Please enter your password");
                } else if (TextUtils.isEmpty(confirmPassText)) {
                    confirmPasswordTL.setError("Please enter your password again");
                } else if (!passwordText.equals(confirmPassText)) {
                    confirmPasswordTL.setError("Please make sure passwords are identical");
                } else {
                    Register(nameText, emailText, passwordText);
                }
            }
        });
    }

    //register user using firebase
    private void Register(final String name, final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        nameTL.setErrorEnabled(false);
                        emailTextTL.setErrorEnabled(false);
                        passwordTextTL.setErrorEnabled(false);
                        confirmPasswordTL.setErrorEnabled(false);

                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userId = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference("Users")
                                    .child(userId);

                            //hashmap used to store user information
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("name", name);
                            hashMap.put("email", email);

                            //open category activity after registering successfully
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent i = new Intent(SignUpActivity.this, CategoriesActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    } else {
                                        task.getException().printStackTrace();
                                        emailTextTL.setError("Please make sure this is a valid email address");
                                        passwordTextTL.setError("Please make sure your password's longer than 5 characters");
                                    }
                                }
                            });
                        }
                    }
                });
    }
}