package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    public static final String url = "http://e411ad1fadd2.ngrok.io";
    TextInputEditText username;
    TextInputEditText password;

    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String user = sp.getString("UserName", null);
        Intent currentIntent;
        if (user==null) {
            currentIntent = new Intent(getApplicationContext(), LoginActivity.class);
        }else {
            currentIntent = new Intent(getApplicationContext(), HomeActivity.class);
        }
        startActivityForResult(currentIntent, 0);
    }

}
