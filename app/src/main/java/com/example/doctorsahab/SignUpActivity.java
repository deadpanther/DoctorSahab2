package com.example.doctorsahab;

import android.app.ProgressDialog;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    EditText emailID, pwd;
    Button sign_button;
    TextView to_login;
    private FirebaseAuth mAuth;
//    ProgressDialog progressDialog = new ProgressDialog(this);


    private void checkSession() {
        SessionManagement sessionManagement = new SessionManagement(SignUpActivity.this);
        String userID = sessionManagement.getSession();

        if(!userID.equals("NOTLOGGEDIN")){
            moveToMainActivity();
        }
    }

    private void moveToMainActivity() {
        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        checkSession();

        mAuth = FirebaseAuth.getInstance();
        emailID = findViewById(R.id.username);
        pwd = findViewById(R.id.password);
        sign_button = findViewById(R.id.signup);
        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailID.getText().toString();
                String password = pwd.getText().toString();
                if(email.isEmpty())
                {
                    emailID.setError("Please enter your email");
                }
                else if (password.isEmpty()){
                    pwd.setError("Please enter your password");
                }
                else if(email.isEmpty() && password.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Fields are empty", Toast.LENGTH_LONG).show();
                }
                else if(!email.isEmpty() && !password.isEmpty()){
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(SignUpActivity.this,"SignUp unsuccessful", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(SignUpActivity.this,"User Signed Up!", Toast.LENGTH_LONG).show();
                                String userID = mAuth.getCurrentUser().getUid();
                                String emailID = mAuth.getCurrentUser().getEmail();
                                User user = new User(userID,emailID);
                                SessionManagement sessionManagement = new SessionManagement(SignUpActivity.this);
                                sessionManagement.saveSession(user);
                                moveToMainActivity();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(SignUpActivity.this,"Error Occurred!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void onClick(View view) {
        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(i);
    }
}
