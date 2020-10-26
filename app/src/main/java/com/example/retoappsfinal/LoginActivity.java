package com.example.retoappsfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    private EditText userText;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userText = findViewById(R.id.username_text);
        loginBtn = findViewById(R.id.login_btn);

        ActivityCompat.requestPermissions(this, new  String[] {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, 1
                );

        loginBtn.setOnClickListener(
                (v) -> {
                    String user = userText.getText().toString();
                    Intent i = new Intent(this, MapsActivity.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }
        );

    }
}