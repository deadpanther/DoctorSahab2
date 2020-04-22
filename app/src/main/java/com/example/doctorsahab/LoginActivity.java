package com.example.doctorsahab;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText emailID, pwd;
    private TextView to_signup;
    private FirebaseAuth mAuth;
    //FirebaseUser currentUser = mAuth.getCurrentUser();

    private void checkSession() {
        //check if user is logged in
        //if user is logged in --> move to mainActivity

        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
        String userID = sessionManagement.getSession();

        if(!userID.equals("NOTLOGGEDIN"))
        {
            moveToMainActivity();
        }
    }


    private void moveToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        checkSession();

        mAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.email);
        pwd = findViewById(R.id.password);
        Button login_button = findViewById(R.id.login);
        to_signup = findViewById(R.id.to_signup);
        //to_signup = findViewById(R.id.to_signup);
        //progressDialog.findViewById(R.id.loading);
        //to_login = findViewById(R.id.to_login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailID.getText().toString();
                String password = pwd.getText().toString();
                if (email.isEmpty()) {
                    emailID.setError("Please enter your email");
                } else if (password.isEmpty()) {
                    pwd.setError("Please enter your password");
                } else if (email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Fields are empty", Toast.LENGTH_LONG).show();
                } else if (!email.isEmpty() && !password.isEmpty()) {

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this, "Error while logging in", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "User Logged In!", Toast.LENGTH_LONG).show();
                                        String userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                        String emailID = mAuth.getCurrentUser().getEmail();
                                        User u = new User(userID, emailID);
                                        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                                        sessionManagement.saveSession(u);
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        finish();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(LoginActivity.this, "Error Occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void onClick(View view) {
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);
    }
}
