package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

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
                login(username.getText().toString(),password.getText().toString());

            }
        });
    }

    public void loginRespones(String respone){
        try {
            JSONObject obj = new JSONObject(respone);
            Toast.makeText(getApplicationContext(), obj.get("message").toString(),Toast.LENGTH_SHORT).show();
            if (obj.get("status").toString().equals("0")) {
                SharedPreferences sp=getSharedPreferences("Login", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("UserName",obj.get("user_name").toString());
                Ed.putString("token",obj.get("token").toString());
                Ed.apply();
                Intent loginIntent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivityForResult(loginIntent, 0);
            }

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + respone + "\"");
        }
    }

    public void login(String username, String password) {
        String url = "http://e8fe63b2ecdc.ngrok.io/login";
//        String url ="https://www.google.com";
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("user_name", username);
        postParam.put("password", password);
        JSONObject jsonBody = new JSONObject(postParam);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    loginRespones(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    System.out.println(error.getMessage());
//                    String respone = "{\"status\":\"1\",\"message\":\"login success\",\"user_name\":\"khiempm1\",\"token\":\"ancedsvs:dbskcn:dkmcdcd:dkmcdkmc\"}";
//                    loginRespones(respone);
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody()  {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
            };

            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}