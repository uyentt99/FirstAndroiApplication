package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {
    TextInputEditText username;
    TextInputEditText password;

    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String user = sp.getString("UserName", null);
        Intent currentIntent;
        if (user==null) {
            currentIntent = new Intent(getApplicationContext(), LoginActivity.class);
        }else {
            System.out.println("TEST");;
            System.out.println(user);
            currentIntent = new Intent(getApplicationContext(), HomeActivity.class);
        }
        startActivityForResult(currentIntent, 0);
    }


}
