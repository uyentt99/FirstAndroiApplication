package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class HeaderActivity extends AppCompatActivity {
    TextView textViewName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setHeaderView(){
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String user = sp.getString("UserName", null);
        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewName.setText(user);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void xemThietBi(){
        Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivityForResult(loginIntent,0);
    }

    public void themThietBi(){
        Intent loginIntent = new Intent(getApplicationContext(), AddActivity.class);
        startActivityForResult(loginIntent,0);
    }

    public void xoaThietBi(){
        Intent loginIntent = new Intent(getApplicationContext(), DeleteActivity.class);
        startActivityForResult(loginIntent,0);
    }

    public void logOut(){
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
        finish();
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(loginIntent,0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menuItem1:
                xemThietBi();
                return true;
            case R.id.menuItem2:
                themThietBi();
                return true;
            case R.id.menuItem3:
                xoaThietBi();
                return true;
            case R.id.menuItem4:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}