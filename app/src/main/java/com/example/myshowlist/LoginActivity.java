package com.example.myshowlist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mRegisterBtn;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mLoginBtn = findViewById(R.id.login_btn);
        mRegisterBtn = findViewById(R.id.register_btn_txt);

        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        mRegisterBtn.setOnClickListener(v -> {

            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        });

        mLoginBtn.setOnClickListener(v -> {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            if(TextUtils.isEmpty(email)){
                mEmail.setError("Email is required");
                return;
            }

            else if (TextUtils.isEmpty(password)){
                mPassword.setError("Password is required");
                return;
            }

            else if (password.length() < 5){
                mPassword.setError("Password must have more than 5 characters");
            }

            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {

                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"User logged successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Toast.makeText(LoginActivity.this,"Error:" + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            });

        });
    }


}
