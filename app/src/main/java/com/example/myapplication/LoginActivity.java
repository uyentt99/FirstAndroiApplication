package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputEditText password;

    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (TextInputEditText) findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("admin") &&
                        password.getText().toString().equals("admin")) {
                }else {

                }
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("UserName",username.getText().toString() );
                Ed.putString("Password",password.getText().toString());
                Ed.apply();
                Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivityForResult(loginIntent, 0);
            }
        });
    }
}