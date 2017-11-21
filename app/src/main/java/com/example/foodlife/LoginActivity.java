package com.example.foodlife;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button btnSignIn = (Button) findViewById(R.id.btnSignIn);
        final Button btnSignOut = (Button) findViewById(R.id.btnSignOut);
        final Button btnGoogle = (Button) findViewById(R.id.googleSignIn);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignIn.setVisibility(View.VISIBLE);
                btnSignOut.setVisibility(View.GONE);
                btnGoogle.setVisibility(View.VISIBLE);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newMenuActivity = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(newMenuActivity);
            }
        });
    }
}
