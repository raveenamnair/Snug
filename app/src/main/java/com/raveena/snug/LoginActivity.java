package com.raveena.snug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class LoginActivity extends AppCompatActivity {
    //widgets
    EditText usernameET, passwordET;
    Button loginBtn, signupBtn;
    TextInputLayout emailTextTL, passwordTextTL;

    //firebase
    FirebaseAuth firebaseAuth;

    //used to save current logged in user so don't need to login every time app opens
    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //saving current user
        if (firebaseUser != null) {
            Intent i = new Intent(LoginActivity.this, CategoriesActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize
        usernameET = findViewById(R.id.emailLogin);
        passwordET = findViewById(R.id.passwordLogin);
        loginBtn = findViewById(R.id.loginBtn);
        signupBtn = findViewById(R.id.signUpBtn);
        emailTextTL = findViewById(R.id.emailTextTL);
        passwordTextTL = findViewById(R.id.passwordTextTL);

        //Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //automatically goes to homepage if user already logged in before, saves current user
        if (firebaseUser != null) {
            Intent i = new Intent(LoginActivity.this, CategoriesActivity.class);
            startActivity(i);
            finish();
        }

        //sign up button
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        //Login button
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailTextTL.setErrorEnabled(false);
                passwordTextTL.setErrorEnabled(false);

                final String emailText = usernameET.getText().toString();
                String passwordText = passwordET.getText().toString();

                //Check if any fields are empty
                if (TextUtils.isEmpty(emailText)) {
                    emailTextTL.setError("Please enter your email address");
                } else if (TextUtils.isEmpty(passwordText)) {
                    passwordTextTL.setError("Please enter your password");
                }
                else {
                    firebaseAuth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    Log.d("login", task.getException().getMessage());
                                    if(task.isSuccessful()) {
                                        Intent i = new Intent(LoginActivity.this, CategoriesActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                        finish();
                                    }
                                    else {
                                        emailTextTL.setError("Possible incorrect email");
                                        passwordTextTL.setError("Possible incorrect password");
                                    }
                                }
                            });
                }
            }
        });
    }
}