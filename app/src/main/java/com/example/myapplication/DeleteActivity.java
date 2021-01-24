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
import com.android.volley.DefaultRetryPolicy;
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

public class DeleteActivity extends HeaderActivity {
    TextInputEditText device_code;

    Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        setHeaderView();

        device_code = (TextInputEditText) findViewById(R.id.device_code);
        deleteButton = (Button)findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDeleteRequest(device_code.getText().toString());
            }
        });
    }

    public void getDeleteRespones(String response){
        try {
            JSONObject obj = new JSONObject(response);
            System.out.println("Response " + obj.toString());
            Toast.makeText(getApplicationContext(), obj.get("message").toString(),Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
        }
    }

    public void sendDeleteRequest(String device_code) {
        String url = MainActivity.url+"/unpairDevice";
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String token = sp.getString("token", null);
        System.out.println("Token "+token);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("devicecode", device_code);
        JSONObject jsonBody = new JSONObject(postParam);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    getDeleteRespones(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    System.out.println(error.getMessage());
                    Toast.makeText(getApplicationContext(), "Connect Error",Toast.LENGTH_SHORT).show();
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
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("Content-Type","application/json; charset=utf-8");
                    header.put("Authorization","Bearer "+token);
                    return header;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Connect Error",Toast.LENGTH_SHORT).show();
        }
    }

}