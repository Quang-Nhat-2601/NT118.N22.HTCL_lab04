package com.example.lab4_signinform;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    EditText signinUsername, signinPassword;
    TextView signinBtn;
    FirebaseDatabase db;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("users");

        signinUsername = findViewById(R.id.signinUsername);
        signinPassword = findViewById(R.id.signinPassword);
        signinBtn = findViewById(R.id.signinBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUser();
            }
        });


    }

    private void checkUser() {
        String username = signinUsername.getText().toString();
        String password = signinPassword.getText().toString();

        Query checkUserDb = reference.orderByChild("username").equalTo(username);

        checkUserDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    signinUsername.setError(null);
                    String passwordFromDB = snapshot.child(username).child("password").getValue(String.class);

                    if(!passwordFromDB.equals(password)) {
                        signinUsername.setError(null);
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(i);
                    } else {
                        signinPassword.setError("Invalid Credentials");
                        signinPassword.requestFocus();
                    }
                } else {
                    signinUsername.setError("Username does not exist");
                    signinUsername.requestFocus();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}